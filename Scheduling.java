package proyecto;
//Jordi Bleizeffer 598255
//Ramses Rojero 603736
// Codigo de Honor: Damos nuestra palabra que hemos realizado esta actividad con integridad academica

import java.io.*;
import java.util.*;

class Scheduling {
    public static void main(String args[]) {
        Scanner s = new Scanner(System.in);
        String algor = "";
        int numInter = 0;
        int numAlgo = 0;
        float contProcesos = 0;         
        float decision = 0;            
        float quantum = 5;
        float reloj = 0;
        int validar = 0;
        while (validar == 0){
            System.out.println("""
                                ¿Cual algoritmo utilizara?
                                1. FIFO -- First In First Out
                                2. RR -- Round Robin
                                3. SRT -- Shortest Remaining Time
                                4. HRRN -- Highest Respond-Ratio Next""");
            try {
                numAlgo = s.nextInt();
                switch (numAlgo) {
                    case 1 -> {
                        algor = "FIFO";
                        validar = 1;
                    }
                    case 2 -> {
                        algor = "Round Robin";
                        validar = 1;
                    }
                    case 3 -> {
                        algor = "SRT";
                        validar = 1;
                    }
                    case 4 -> {
                        algor = "HRRN";
                        validar = 1;
                    }
                    default -> System.out.println("Ingrese un numero valido");
                }
            } catch (Exception e) {
                System.out.println("Ingrese un numero valido");
                s.nextLine();
            }
        }
        float[][] pcb = new float[6][11]; 
        float[][] pcbRes = new float[6][11];
        pcb[0][0] = 0;
        pcb[1][0] = 1;
        pcb[2][0] = 2;
        pcb[3][0] = 3;
        pcb[4][0] = 4;
        pcb[5][0] = 5;
        ArrayList<Float> readyList = new ArrayList<Float>();       
        ArrayList<Float> blockedList = new ArrayList<Float>();     
        ArrayList<Float> finishedList = new ArrayList<Float>();   
        ArrayList<Float> runningList = new ArrayList<Float>();

        try {
            File file = new File("C:\\Users\\rojero\\OneDrive\\Documents\\Trabajos\\Tarea-Trabajos Universidad\\6 Sem\\Sistemas Operativos\\Proyectos\\src\\proyecto\\kernELI PONCErativos.txt");
            Scanner sc = new Scanner(file);
            String data1 = "0";
            while (sc.hasNextLine()) {

                data1 = sc.nextLine();
                String[] div = data1.split(", ", 2);
                String a = div[0];
                String b = div[1];

                float numMaxPag = Integer.parseInt(a);  
                reloj = Integer.parseInt(b);    

                String nProc = sc.nextLine();
                float numProcesos = Integer.parseInt(nProc); 
                String data2 = "0";
                for (int i = 0; i < numProcesos; i++) {
                    contProcesos++;

                    data2 = sc.nextLine();
                    String[] div2 = data2.split(", ", 3);
                    String c = div2[0];
                    String d = div2[1];
                    String e = div2[2];
                    float tiempLlegada = Integer.parseInt(c);
                    float tiempEstimado = Integer.parseInt(d);
                    float estado = Integer.parseInt(e); 
                    String nPag = sc.nextLine();
                    float numPag = Integer.parseInt(nPag);
                    int cont = 0;
                    do {
                        cont++;
                        String datosMuertos = sc.nextLine(); //sacar luego

                    } while (cont < numPag);

                    pcb[i][1] = contProcesos;
                    pcb[i][2] = tiempLlegada;
                    pcb[i][3] = tiempEstimado;
                    pcb[i][8] = tiempEstimado;        
                    pcb[i][4] = estado;
                    pcb[i][5] = numPag;
                    float envej = reloj - tiempLlegada;
                    pcb[i][7] = envej;
                    pcb[i][9] = quantum;
                    pcb[i][10] = (envej + tiempEstimado) / tiempEstimado;
                }
            }
        } catch (Exception e) {
            System.out.println("");
        }

        respPcb(pcb, pcbRes);
        sortPcb(pcb, readyList, blockedList, runningList, pcbRes);
        switch (algor) {
            case "SRT" -> sortPcbSRT(pcb, readyList, runningList, pcbRes);
            case "HRRN" -> sortPcbHRRN(pcb, readyList, runningList, pcbRes);
            default -> {
            }
        }
        printPcbMain(pcb, readyList, blockedList, runningList, finishedList, reloj, algor);
        while (decision != 4) {
            System.out.println("""
                                1. Ejecutar
                                2. Interrupcion
                                3. Nuevo Proceso Proceso
                                4. Terminar Simulacion""");
            try {
                decision = s.nextInt();
                if (decision == 1) {
                    ejecutarpcb(runningList, readyList, finishedList, pcb, algor, pcbRes);
                    reloj++;
                    actEnvej(pcb, reloj);
                    actHRRN(pcb);
                    respPcb(pcb, pcbRes);
                    printPcbMain(pcb, readyList, blockedList, runningList, finishedList, reloj, algor);
                } else if (decision == 2) {
                    boolean salida = false;
                    while (salida == false) {
                        System.out.println("""
                                            Que interrupcion quiere?
                                            1. SVC Solicitud de I/O
                                            2. SVC de terminacion normal
                                            3. SVC de solicitud de fecha
                                            4. Error de programa
                                            5. Quantum Expirado
                                            6. Dispositivo de I/O""");
                        try {
                            numInter = s.nextInt();
                            if (numInter <= 0 || numInter > 7) {
                                System.out.println("Ingrese un numero disponiblenible");
                            } else {
                                interupMenu(pcb, readyList, blockedList, runningList, finishedList, numInter, reloj, algor, pcbRes);
                                reloj++;
                                actEnvej(pcb, reloj);
                                actHRRN(pcb);
                                respPcb(pcb, pcbRes);
                                switch (algor) {
                                    case "SRT" -> sortPcbSRT(pcb, readyList, runningList, pcbRes);
                                    case "HRRN" -> sortPcbHRRN(pcb, readyList, runningList, pcbRes);
                                    default -> {
                                    }
                                }
                                salida = true;
                                printPcbMain(pcb, readyList, blockedList, runningList, finishedList, reloj, algor);
                            }
                        } catch (Exception e) {
                            System.out.println("Ingrese un numero valido");
                            s.nextLine();
                        }
                    }
                } else if (decision == 3) {
                    int total = readyList.size() + runningList.size() + blockedList.size();
                    if (total == 6) {
                        System.out.println("Ya esta el limite de procesos a poner");
                    } else {
                        contProcesos++;
                        float est = 0f;
                        int disponible = 0;
                        boolean continuar = false;
                        for (int i = 0; i < 6; i++) {
                            if (pcb[i][1] == 0) {
                                disponible = Math.round(pcb[i][0]);
                                break;
                            }
                        }
                        pcb[disponible][1] = contProcesos;
                        pcb[disponible][2] = reloj;
                        while (continuar == false) {
                            System.out.println("Ingrese el tiempo estimado de ejecucion");
                            try {
                                est = s.nextFloat();
                                continuar = true;
                            } catch (Exception e) {
                                System.out.println("Ingrese un numero valido");
                                s.nextLine();
                            }
                        }
                        pcb[disponible][3] = est;
                        pcb[disponible][4] = 3;
                        pcb[disponible][5] = 5;
                        pcb[disponible][7] = 0;
                        pcb[disponible][8] = est;
                        pcb[disponible][9] = 5;
                        pcb[disponible][10] = 0;
                        readyList.add(pcb[disponible][1]);
                        System.out.println("Se ha agregado un proceso");
                        printPcbMain(pcb, readyList, blockedList, runningList, finishedList, reloj, algor);
                    }
                } else if (decision == 4) {
                    System.out.println("Simulacion Terminada");
                } else {
                    System.out.println("Ingrese un numero valido");
                }
            } catch (Exception e) {
                System.out.println("Ingrese un numero valido");
                s.nextLine();
            }
        }
    }

    public static void sortPcb(float[][] pcb, ArrayList<Float> readyList, ArrayList<Float> blockedList, ArrayList<Float> runningList, float[][] pcbRes) {
        ArrayList<Float> readyListTemp = new ArrayList<Float>();
        ArrayList<Float> blockedListTemp = new ArrayList<Float>();
        for (int i = 0; i < 6; i++) {
            if (pcb[i][2] != 0 && pcb[i][4] == 3) {
                readyListTemp.add(pcb[i][2]);
            } else if (pcb[i][2] != 0 && pcb[i][4] == 2) {
                blockedListTemp.add(pcb[i][2]);
            } else if (pcb[i][2] != 0 && pcb[i][4] == 1) {
                runningList.add(pcb[i][1]);
            }
        }
        Collections.sort(readyListTemp);
        Collections.sort(blockedListTemp);

        for (int i = 0; i < readyListTemp.size(); i++) {
            for (int j = 0; j < 6; j++) {
                if (readyListTemp.get(i) == pcbRes[j][2] && pcb[j][4] == 3) {
                    readyList.add(pcbRes[j][1]);
                    pcbRes[j][2] = 0;
                    break;
                }
            }
        }

        for (int i = 0; i < blockedListTemp.size(); i++) {
            for (int j = 0; j < 6; j++) {
                if (blockedListTemp.get(i) == pcbRes[j][2] && pcb[j][4] == 2) {
                    blockedList.add(pcb[j][1]);
                    pcbRes[j][2] = 0;
                    break;
                }
            }
        }
        respPcb(pcb, pcbRes);
    }

    public static void sortPcbReady(float[][] pcb, ArrayList<Float> readyList, ArrayList<Float> blockedList, ArrayList<Float> runningList, float[][] pcbRes) { //Ordena procesos por tiempo de llegada
        ArrayList<Float> readyListTemp = new ArrayList<Float>();
        ArrayList<Float> blockedListTemp = new ArrayList<Float>();
        for (int i = 0; i < 6; i++) {
            if (pcb[i][2] != 0 && pcb[i][4] == 3) {
                readyListTemp.add(pcb[i][2]);
            }
        }
        Collections.sort(readyListTemp);

        for (int i = 0; i < readyListTemp.size(); i++) {
            for (int j = 0; j < 6; j++) {
                if (readyListTemp.get(i) == pcbRes[j][2] && pcb[j][4] == 3) {
                    readyList.add(pcbRes[j][1]);
                    pcbRes[j][2] = 0;
                    break;
                }
            }
        }
        respPcb(pcb, pcbRes);
    }

    public static void sortPcbSRT(float[][] pcb, ArrayList<Float> readyList, ArrayList<Float> runningList, float[][] pcbRes) {
        ArrayList<Float> readyListSRT = new ArrayList<Float>();
        for (int i = 0; i < 6; i++) {
            if (pcb[i][2] != 0 && pcb[i][4] == 3) {
                readyListSRT.add(pcb[i][8]);
            }
        }
        readyList.clear();
        Collections.sort(readyListSRT);
        for (int i = 0; i < readyListSRT.size(); i++) {
            for (int j = 0; j < 6; j++) {
                if (readyListSRT.get(i) == pcbRes[j][8] && pcb[j][4] == 3) {
                    readyList.add(pcb[j][1]);
                    pcbRes[j][8] = 0;
                    break;
                }
            }
        }
        respPcb(pcb, pcbRes);
    }

    public static void sortPcbHRRN(float[][] pcb, ArrayList<Float> readyList, ArrayList<Float> runningList, float[][] pcbRes) {
        System.out.println("Sort: HRRN");
        ArrayList<Float> readyListHRRN = new ArrayList<Float>();
        for (int i = 0; i < 6; i++) {
            if (pcb[i][2] != 0 && pcb[i][4] == 3) {
                readyListHRRN.add(pcb[i][10]);
            }
        }
        readyList.clear();
        Collections.sort(readyListHRRN);
        System.out.println(readyListHRRN);
        for (int i = 0; i < readyListHRRN.size(); i++) {
            for (int j = 0; j < 6; j++) {
                if (readyListHRRN.get(i) == pcbRes[j][10] && pcb[j][4] == 3) {
                    readyList.add(pcb[j][1]);
                    pcbRes[j][10] = 0;
                    break;
                }
            }
        }
        respPcb(pcb, pcbRes);
    }

    public static void ejecutarpcb(ArrayList<Float> runningList, ArrayList<Float> readyList, ArrayList<Float> finishedList, float[][] pcb, String algor, float[][] pcbRes) {
        if (runningList.isEmpty()) {
            System.out.println("Running: No hay proceso");
        } else if (algor.equals("FIFO")) {
            int runTemp = 0;
            for (int i = 0; i < 6; i++) {
                if (pcb[i][4] == 1.0) {
                    runTemp = Math.round(pcb[i][0]);
                }
            }
            pcb[runTemp][6]++;
            pcb[runTemp][8]--;
            if (pcb[runTemp][6] == pcb[runTemp][3]) {
                finishedList.add(runningList.get(0));
                runningList.remove(0);
                for (int i = 1; i < 11; i++) {
                    pcb[runTemp][i] = 0;
                }
                readyArun(runningList, readyList, pcb);
            }
        } else if (algor.equals("Round Robin")) {
            int runTemp = 0;
            for (int i = 0; i < 6; i++) {
                if (pcb[i][4] == 1.0) {
                    runTemp = Math.round(pcb[i][0]);
                }
            }
            pcb[runTemp][6]++;
            pcb[runTemp][8]--;
            pcb[runTemp][9]--;
            if (pcb[runTemp][6] == pcb[runTemp][3]) {
                finishedList.add(runningList.get(0));
                runningList.remove(0);
                for (int i = 1; i < 11; i++) {
                    pcb[runTemp][i] = 0;
                }
                readyArun(runningList, readyList, pcb);
            } else if (pcb[runTemp][9] == 0) {
                pcb[runTemp][9] = 5;
                runAready(runningList, readyList, pcb);
                readyArun(runningList, readyList, pcb);
            }
        } else if (algor.equals("SRT")) {
            int runTemp = 0;
            for (int i = 0; i < 6; i++) {
                if (pcb[i][4] == 1.0) {
                    runTemp = Math.round(pcb[i][0]);                
                    }
            }
            pcb[runTemp][6]++;
            pcb[runTemp][8]--;
            if (pcb[runTemp][6] == pcb[runTemp][3]) {
                finishedList.add(runningList.get(0));
                runningList.remove(0);
                for (int i = 1; i < 11; i++) {
                    pcb[runTemp][i] = 0;
                }
                readyArun(runningList, readyList, pcb);
            }
            sortPcbSRT(pcb, readyList, runningList, pcbRes);
        } else if (algor.equals("HRRN")) {
            System.out.println("Ejecutar HRRN");
            int runTemp = 0;
            for (int i = 0; i < 6; i++) {
                if (pcb[i][4] == 1.0) {
                    runTemp = Math.round(pcb[i][0]);
                }
            }
            pcb[runTemp][6]++;
            pcb[runTemp][8]--;
            if (pcb[runTemp][6] == pcb[runTemp][3]) {
                finishedList.add(runningList.get(0));
                runningList.remove(0);
                for (int i = 1; i < 11; i++) {
                    pcb[runTemp][i] = 0;
                }
                readyArun(runningList, readyList, pcb);
            }
            sortPcbHRRN(pcb, readyList, runningList, pcbRes);
        }
    }

    public static void readyArun(ArrayList<Float> runningList, ArrayList<Float> readyList, float[][] pcb) {
        float readyTemp;
        int readyTempInt = 0;
        if (readyList.isEmpty()) {
            System.out.println("Ready: No hay procesos");
        } else {
            runningList.add(readyList.get(0));
            readyTemp = readyList.get(0);
            for (int i = 0; i < 6; i++) {
                if (pcb[i][1] == readyTemp) {
                    readyTempInt = Math.round(pcb[i][0]);
                }
            }
            pcb[readyTempInt][4] = 1;
            readyList.remove(0);
            System.out.println("Running: Proceso ha llegado");
        }
    }

    public static void runAready(ArrayList<Float> runningList, ArrayList<Float> readyList, float[][] pcb) {
        if (runningList.isEmpty()) {
            System.out.println("Running: No hay proceso");
        } else {
            readyList.add(runningList.get(0));
            int runTemp = 0;
            for (int i = 0; i < 6; i++) {
                if (pcb[i][4] == 1.0) {
                    runTemp = Math.round(pcb[i][0]);
                }
            }
            pcb[runTemp][4] = 3;
            runningList.remove(0);
            System.out.println("Ready: Proceso ha llegado");
        }
    }

    public static void runAfin(ArrayList<Float> runningList, ArrayList<Float> readyList, ArrayList<Float> finishedList, float[][] pcb) {
        if (runningList.isEmpty()) {
            System.out.println("Running: No hay proceso");
        } else {
            int runTemp = 0;
            for (int i = 0; i < 6; i++) {
                if (pcb[i][4] == 1.0) {
                    runTemp = Math.round(pcb[i][0]);
                }
            }
            finishedList.add(runningList.get(0));
            runningList.remove(0);
            for (int i = 1; i < 11; i++) {
                pcb[runTemp][i] = 0;
            }
            System.out.println("Finished: Proceso ha llegado");
        }
    }

    public static void runAblock(ArrayList<Float> runningList, ArrayList<Float> readyList, ArrayList<Float> blockedList, float[][] pcb) {
        if (runningList.isEmpty()) {
            System.out.println("Running: No hay proceso");
        } else {
            blockedList.add(runningList.get(0));
            int runTemp = 0;
            for (int i = 0; i < 6; i++) {
                if (pcb[i][4] == 1.0) {
                    runTemp = Math.round(pcb[i][0]);
                }
            }
            pcb[runTemp][4] = 2;
            runningList.remove(0);
            System.out.println("Blocked: Proceso ha llegado");
        }
    }

    public static void blockedAready(ArrayList<Float> blockedList, ArrayList<Float> readyList, float[][] pcb) {
        float blockTemp;
        int blockTempInt = 0;
        if (blockedList.isEmpty()) {
            System.out.println("No hay procesos en Blocked!");
        } else {
            readyList.add(blockedList.get(0));
            blockTemp = blockedList.get(0);
            for (int i = 0; i < 6; i++) {
                if (pcb[i][1] == blockTemp) {
                    blockTempInt = Math.round(pcb[i][0]);
                }
            }
            pcb[blockTempInt][4] = 3;
            blockedList.remove(0);
            System.out.println("Proceso movido a Ready!");
        }
    }

    public static void interupMenu(float[][] pcb, ArrayList<Float> readyList, ArrayList<Float> blockedList, ArrayList<Float> runningList, ArrayList<Float> finishedList, int numInter, float reloj, String algor, float[][] pcbRes) {
        switch (numInter) {
            case 1 -> { //SVC Solicitud de I/O
                runAblock(runningList, readyList, blockedList, pcb);
                readyArun(runningList, readyList, pcb);
            }
            case 2 -> {//SVC de terminacion normal
                runAfin(runningList, readyList, finishedList, pcb);
                readyArun(runningList, readyList, pcb);
            }
            case 3 -> {//SVC de solicitud de fecha
                runAblock(runningList, readyList, blockedList, pcb);
                readyArun(runningList, readyList, pcb);
            }
            case 4 -> {//Error del programa
                runAfin(runningList, readyList, finishedList, pcb);
                readyArun(runningList, readyList, pcb);
            }
            case 5 -> {//Quantum Expirado
                runAready(runningList, readyList, pcb);
                readyArun(runningList, readyList, pcb);
            }
            case 6 -> {//Dispositivo de I/O
                blockedAready(blockedList, readyList, pcb);
                if (runningList.isEmpty()) {
                    readyArun(runningList, readyList, pcb);
                } else {
                    ejecutarpcb(runningList, readyList, finishedList, pcb, algor, pcbRes);
                }
            }
            default -> {
            }
        }
    }
    
        public static void respPcb(float[][] pcb, float[][] pcbRes) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 11; j++) {
                pcbRes[i][j] = pcb[i][j];
            }
        }
    }

    public static void actEnvej(float[][] pcb, float reloj) {
        float envejAct = 0;
        for (int i = 0; i < 6; i++) {
            if (pcb[i][4] == 3) {
                envejAct = reloj - pcb[i][2] - pcb[i][6];
                pcb[i][7] = envejAct;
            }
        }
    }

    public static void actHRRN(float[][] pcb) {
        for (int i = 0; i < 6; i++) {
            if (pcb[i][4] == 3) {
                pcb[i][10] = (pcb[i][7] + pcb[i][3]) / pcb[i][3];
            }
        }
    }

    public static void printPcbMain(float[][] pcb, ArrayList<Float> readyList, ArrayList<Float> blockedList, ArrayList<Float> runningList, ArrayList<Float> finishedList, float reloj, String algor) {
        int runTemp = 0;
        for (int i = 0; i < 6; i++) {
            if (pcb[i][4] == 1.0) {
                runTemp = Math.round(pcb[i][0]);
            }
        }
        int procAct = Math.round(pcb[runTemp][1]);
        int tLlegada = Math.round(pcb[runTemp][2]);
        int cpuAsig = Math.round(pcb[runTemp][6]);
        int envejec = Math.round(pcb[runTemp][7]);
        int cpuRest = Math.round(pcb[runTemp][8]);
        int quantumRest = Math.round(pcb[runTemp][9]);
        
        System.out.println("");
        System.out.println("--           Proyecto SO p1           --");
        System.out.println("Tiempo Actual: " + reloj);
        System.out.println(" ");
        System.out.println("Procesos");
        System.out.println("Running: " + runningList);
        System.out.println("Ready: " + readyList);
        System.out.println("Blocked: " + blockedList);
        System.out.println("Finished: " + finishedList);
        System.out.println(" ");
        System.out.println("Scheduling");
        System.out.println("Proceso actual: " + procAct);
        System.out.println("Tiempo llegada: " + tLlegada);
        System.out.println("Envejecimiento: " + envejec);
        System.out.println("CPU Asignado: " + cpuAsig);
        System.out.println("CPU Restante: " + cpuRest);
        System.out.println("Quantum Restante: " + quantumRest);
        System.out.println(" ");
        System.out.println("Tipo de Algoritmo: " + algor);
        System.out.println("");
    }
}
