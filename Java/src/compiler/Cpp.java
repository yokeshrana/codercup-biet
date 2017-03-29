
package compiler;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author hexnor
 */
class Cpp extends exec {
	
	String file, contents, dir;
	int timeout;
	
	public Cpp(String file, int timeout, String writtencode, String absolutePath) {
        this.file = file;
		this.timeout = timeout;
		this.contents = writtencode;
		this.dir = absolutePath;
    
	}
	public void compile() {
		try {
                    
//                           Thread.sleep(1000);
//                           System.out.println("Execution continuing");
                    
                    
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + file)));
			out.write(contents);
			out.close();
			// create the compiler script
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/compile.sh")));
			out.write("cd \"" + dir +"\"\n");
			out.write("g++ -std=c++11 -lm " + file + " 2> err.txt");
			out.close();
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("chmod +x " + dir + "/compile.sh");
			p.waitFor();
			p = r.exec(dir + "/compile.sh"); // execute the compiler script
			p.waitFor();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void execute() {
		try {
			// create the execution script
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/run.sh")));
			out.write("cd \"" + dir +"\"\n");
			out.write("chroot .\n");
			out.write("./a.out < in.txt > out.txt");
			out.close();
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("chmod +x " + dir + "/run.sh");
			p.waitFor();
			p = r.exec(dir + "/run.sh"); // execute the script
			Shell shell = new Shell(this, p, 3000);
			shell.start();
			p.waitFor();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}