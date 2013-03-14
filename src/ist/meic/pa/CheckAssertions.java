package ist.meic.pa;

import javassist.ClassPool;
import javassist.tools.reflect.Loader;

public class CheckAssertions {

	public static void main(String[] args) throws Throwable {
		if (args.length < 1) {
			System.err.println("Usage: Java CheckAssertions <class> <arguments>");
			System.exit(1);
		}

		String className = args[0];

		ClassPool pool = ClassPool.getDefault();
		AssertionsTranslator translator = new AssertionsTranslator();
		Loader classLoader = new Loader();
		classLoader.addTranslator(pool, translator);
			
		String[] methodArgs =  new String[args.length -1 ];
		System.arraycopy(args, 1, methodArgs, 0, methodArgs.length);
		classLoader.run(className, methodArgs);
	}
}
