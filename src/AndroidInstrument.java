import java.util.Collections;
import java.util.List;
import soot.PackManager;
import soot.Scene;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

public class AndroidInstrument{
	public final static String sootJarPath="/Users/apple/Documents/Soot/FlowDroid/sootclasses-trunk-jar-with-dependencies.jar";
	public final static String androidJarPath="/Users/apple/Documents/Soot/android-platforms-master";
	public final static String APKPath="/Users/apple/Documents/Android/version_2.apk";
	//public final static String rtJarPath="/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home/jre/lib/rt.jar";
	
	public static void initSoot(String[] args){
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_android_jars(androidJarPath);
		Options.v().set_process_dir(Collections.singletonList(APKPath));
		Options.v().set_output_format(Options.output_format_none);
		Options.v().set_allow_phantom_refs(true);
		
		Options.v().set_whole_program(true);
		//Options.v().setPhaseOption("cg.spark verbose:true", "on");
		//Options.v().setPhaseOption("cg.spark", "on");
		//Options.v().setPhaseOption("cg.spark", "rta:true");
		//Options.v().setPhaseOption("cg.spark", "on-fly-cg:false");
		Scene.v().loadNecessaryClasses();
	}
	
	public static void main(String[] args) {
		initSoot(args);
		
		SearchTransformer mySearch = new SearchTransformer();
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter",mySearch));
        PackManager.v().runPacks();
        
        CallGraph cg = Scene.v().getCallGraph();
        CGGenerator cgg = new CGGenerator(cg, mySearch.getEntryPoints());
        cgg.explorePoints();
        //cgg.printIntents();
        /*
        RunTransformer myrun = new RunTransformer();
        myrun.init(CGGenerator.entryGraphs, CGGenerator.Points);
        PackManager.v().getPack("jtp").remove("jtp.myInstrumenter");
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter",myrun));
        PackManager.v().runPacks();
        */
        return;
	}
}



