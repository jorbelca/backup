import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CronManager {
    public static String basePath = new File("").getAbsolutePath();

    // Método para eliminar un cron job basado en un identificador
    public static boolean removeCronJob(String jobIdentifier) {
        try {
            // Listar los cron jobs actuales
            Process listCrontab = Runtime.getRuntime().exec(new String[] { "crontab", "-l" });
            BufferedReader reader = new BufferedReader(new InputStreamReader(listCrontab.getInputStream()));

            StringBuilder newCrontab = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // Si la línea contiene el identificador del job, no la incluimos en el nuevo
                // crontab
                if (!line.contains(jobIdentifier)) {
                    newCrontab.append(line).append("\n");
                }
            }

            // Sobrescribir el crontab con la nueva configuración
            Process updateCrontab = Runtime.getRuntime().exec(new String[] { "crontab", "-" });
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(updateCrontab.getOutputStream()));
            writer.print(newCrontab.toString());
            writer.close();
            updateCrontab.waitFor(); // Asegurar que el proceso termine

            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para añadir un cron job si no existe
    public static boolean addCronJob(String cronJob) {
        try {
            // Verificar si el cron job ya existe
            Process listCrontab = Runtime.getRuntime().exec(new String[] { "crontab", "-l" });
            BufferedReader reader = new BufferedReader(new InputStreamReader(listCrontab.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(cronJob)) {
                    // Si ya existe, no hacemos nada
                    return false;
                }
            }

            // Añadir el nuevo cron job
            String[] addJobCommand = { "/bin/bash", "-c", "(crontab -l ; echo \"" + cronJob + "\") | crontab -" };
            Process addJobProcess = Runtime.getRuntime().exec(addJobCommand);
            addJobProcess.waitFor(); // Asegurar que el proceso termine

            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // Identificador único del cron job (podría ser el script o una parte del
        // comando)
        String jobIdentifier = basePath + "/../src/bash/backup_main.sh";

        // Ejemplo de cron job que se ejecuta todos los días a medianoche
        String cronJob = "0 0 * * * /bin/bash " + jobIdentifier;

        // Añadir el cron job
        if (addCronJob(cronJob)) {
            System.out.println("Cron job añadido exitosamente.");
        } else {
            System.out.println("El cron job ya existe o no se pudo añadir.");
        }

        // Eliminar el cron job
        if (removeCronJob(jobIdentifier)) {
            System.out.println("Cron job eliminado exitosamente.");
        } else {
            System.out.println("No se pudo eliminar el cron job.");
        }
    }
}