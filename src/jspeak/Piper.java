package jspeak;

public class Piper implements java.lang.Runnable {

    // Same code as above
    private java.io.InputStream input;

    private java.io.OutputStream output;

    public Piper(java.io.InputStream input, java.io.OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void run() {
        try {
            // Create 512 bytes buffer
            byte[] b = new byte[512];
            int read = 1;
            // As long as data is read; -1 means EOF
            while (read > -1) {
                // Read bytes into buffer
                read = input.read(b, 0, b.length);
                //System.out.println("read: " + new String(b));
                if (read > -1) {
                    // Write bytes to output
                    output.write(b, 0, read);
                }
            }
        } catch (Exception e) {
            // Something happened while reading or writing streams; pipe is broken
            throw new RuntimeException("Broken pipe", e);
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }

    public static java.io.InputStream pipe(java.lang.Process... proc) throws java.lang.InterruptedException {
        // Start Piper between all processes
        java.lang.Process p1;
        java.lang.Process p2;
        for (int i = 0; i < proc.length; i++) {
            p1 = proc[i];
            // If there's one more process
            if (i + 1 < proc.length) {
                p2 = proc[i + 1];
                // Start piper
                new Thread(new Piper(p1.getInputStream(), p2.getOutputStream())).start();
            }
        }
        java.lang.Process last = proc[proc.length - 1];
        // Wait for last process in chain; may throw InterruptedException
        last.waitFor();
        // Return its InputStream
        return last.getInputStream();
    }

}