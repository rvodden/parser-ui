package com.vodden.math.parser.test.utils;

import java.io.IOException;
import java.util.Arrays;

import org.junit.runners.model.InitializationError;

import com.google.inject.Injector;

import cucumber.api.java.ObjectFactory;
import cucumber.api.junit.Cucumber;
import cucumber.runtime.CucumberException;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.java.guice.InjectorSource;
import cucumber.runtime.java.guice.ScenarioScope;
import cucumber.runtime.java.guice.impl.InjectorSourceInstantiationFailed;

/**
 * This is taken from a Stack Overflow question/answer which is here.
 * 
 * The text of the question is:
 * 
 * 
 * I'm using Cucumber with Guice as DI. I've encountered following problem: I've got one step i.e.
 * 
 * <code>
 * class MyStep() {
 * 
 *     {@literal @}Inject
 *     private MyService myService;
 * 
 *     {@literal @}Given("Some acction happen")
 *     public void sthHappen() {
 *         myService.doSth();
 *     }
 * }
 * </code>
 * 
 * And I've got this class to run it as JUnit test
 * 
 * {@literal @}RunWith(Cucumber.class)
 * {@literal @}CucumberOptions(...)
 * public class MyTest {
 * 
 * }
 * There is a
 * 
 * class MyModule extends AbstractModule {
 *     {@literal @}Override
 *     protected void configure() {
 *          bind(MyService.class).to(MyFirstService.class);     
 *     }
 * }
 * which is used by my MyInjectorSource I define cucumber.properties where I define guice.injector-source=MyInjectorSource; There is also a feature file with scenario. Everything is working for now.
 * 
 * And no i would like to run MyStep step with other MyService implementation (of course I don't wont to duplicate code of MyStep) I define a new feature file with new scenarios, and new Test class
 * 
 * {@literal @}RunWith(Cucumber.class)
 * {@literal @}CucumberOptions(...)
 * public class MyOtherTest {
 * 
 * }
 * And now I've tried to create another InjectorSource but I was not able to configure it.
 * 
 * @author kodstark
 * @see <a href="http://stackoverflow.com/questions/35315963/cucumber-with-guice-multiple-guice-injector">The original question.</a>
 *
 */
public class GuiceCucumberRunner extends Cucumber {

    public GuiceCucumberRunner(Class<?> clazz) throws InitializationError, IOException {
        super(clazz);
    }

    @Override
    protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, RuntimeOptions runtimeOptions) throws InitializationError, IOException {
        Runtime result = new Runtime(resourceLoader, classLoader, Arrays.asList(createGuiceBackend()), runtimeOptions);
        return result;
    }

    private JavaBackend createGuiceBackend() {
        GuiceCucumberOptions guiceCucumberOptions = getGuiceCucumberOptions(); 
        InjectorSource injectorSource = createInjectorSource(guiceCucumberOptions.injectorSource());
        ObjectFactory objectFactory = new GuiceFactory(injectorSource.getInjector());
        JavaBackend result = new JavaBackend(objectFactory);
        return result;
    }

    private GuiceCucumberOptions getGuiceCucumberOptions() {
        GuiceCucumberOptions guiceCucumberOptions = getTestClass().getJavaClass().getAnnotation(GuiceCucumberOptions.class);
        if (guiceCucumberOptions == null) {
            String message = String.format("Suite class ''{0}'' is missing annotation GuiceCucumberOptions", getTestClass().getJavaClass());
            throw new CucumberException(message);
        }
        return guiceCucumberOptions;
    }

    private InjectorSource createInjectorSource(Class<? extends InjectorSource> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            String message = String.format("Instantiation of ''{0}'' failed. InjectorSource must have has a public zero args constructor.", clazz);
            throw new InjectorSourceInstantiationFailed(message, e);
        }
    }

    static class GuiceFactory implements ObjectFactory {

        private final Injector injector;

        GuiceFactory(Injector injector) {
            this.injector = injector;
        }

        @Override
        public boolean addClass(Class<?> clazz) {
            return true;
        }

        @Override
        public void start() {
            injector.getInstance(ScenarioScope.class).enterScope();
        }

        @Override
        public void stop() {
            injector.getInstance(ScenarioScope.class).exitScope();
        }

        @Override
        public <T> T getInstance(Class<T> clazz) {
            return injector.getInstance(clazz);
        }
    }
}

