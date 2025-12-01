import java.util.*;

class Process {
    int id, at, bt, prio, deadline;
    int wt, tat;
}

public class CPUScheduler {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        char again;

        do {
            System.out.print("Input no. of processes [2-9]: ");
            int n = sc.nextInt();

            Process p[] = new Process[n];

            System.out.println("Input individual arrival time:");
            for (int i = 0; i < n; i++) {
                p[i] = new Process();
                p[i].id = i + 1;
                System.out.print("AT" + p[i].id + ": ");
                p[i].at = sc.nextInt();
            }

            System.out.println("Input individual burst time:");
            for (int i = 0; i < n; i++) {
                System.out.print("BT" + p[i].id + ": ");
                p[i].bt = sc.nextInt();
            }

            char choice;
            do {
                System.out.println("\nCPU Scheduling Algorithm:");
                System.out.println("[A] First Come First Serve (FCFS)");
                System.out.println("[B] Shortest Job First (SJF)");
                System.out.println("[C] Priority (Prio)");
                System.out.println("[D] Deadline");
                System.out.println("[E] Multilevel Queue (MLQ)");
                System.out.println("[F] Exit");
                System.out.print("\nEnter choice: ");
                choice = sc.next().toUpperCase().charAt(0);

                switch (choice) {
                    case 'A':
                        fcfs(p);
                        break;
                    case 'B':
                        sjf(p);
                        break;
                    case 'C':
                        priority(p);
                        break;
                    case 'D':
                        deadline(p);
                        break;
                    case 'E':
                        mlq(p);
                        break;
                    case 'F':
                        System.out.println("Program terminated.");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } while (choice != 'F');

            System.out.print("\nInput again (y/n)? ");
            again = sc.next().toLowerCase().charAt(0);

        } while (again == 'y');

        System.out.println("Program terminated.");
    }

    // ---------------- ALGORITHMS ------------------

    // ------ FCFS ------
    static void fcfs(Process p[]) {
        Process a[] = copy(p);

        Arrays.sort(a, Comparator.comparingInt(x -> x.at));

        int time = 0;
        for (Process pr : a) {
            time = Math.max(time, pr.at);
            pr.wt = time - pr.at;
            pr.tat = pr.wt + pr.bt;
            time += pr.bt;
        }

        print(a);
    }

    // ------ SJF ------
    static void sjf(Process p[]) {
        Process a[] = copy(p);

        Arrays.sort(a, (x, y) -> {
            if (x.at == y.at) return x.bt - y.bt;
            return x.at - y.at;
        });

        int time = 0;
        boolean done[] = new boolean[a.length];
        int completed = 0;

        while (completed < a.length) {
            int idx = -1, mn = Integer.MAX_VALUE;

            for (int i = 0; i < a.length; i++) {
                if (!done[i] && a[i].at <= time && a[i].bt < mn) {
                    mn = a[i].bt;
                    idx = i;
                }
            }

            if (idx == -1) {
                time++;
                continue;
            }

            a[idx].wt = time - a[idx].at;
            a[idx].tat = a[idx].wt + a[idx].bt;
            time += a[idx].bt;
            done[idx] = true;
            completed++;
        }

        print(a);
    }

    // ------ PRIORITY ------
    static void priority(Process p[]) {
        Process a[] = copy(p);

        System.out.println("Input individual priority number:");
        for (int i = 0; i < a.length; i++) {
            System.out.print("Prio" + a[i].id + ": ");
            a[i].prio = sc.nextInt();
        }

        Arrays.sort(a, (x, y) -> {
            if (x.at == y.at) return x.prio - y.prio;
            return x.at - y.at;
        });

        int time = 0;
        for (Process pr : a) {
            time = Math.max(time, pr.at);
            pr.wt = time - pr.at;
            pr.tat = pr.wt + pr.bt;
            time += pr.bt;
        }

        print(a);
    }

    // ------ DEADLINE ------
    static void deadline(Process p[]) {
        Process a[] = copy(p);

        System.out.println("Input deadline for each process:");
        for (int i = 0; i < a.length; i++) {
            System.out.print("P" + a[i].id + ": ");
            a[i].deadline = sc.nextInt();
        }

        System.out.print("Enter number of output (max 3): ");
        int out = sc.nextInt();
        if (out > 3) out = 3;

        for (int k = 1; k <= out; k++) {
            System.out.println("\nOutput " + k + ":");

            Arrays.sort(a, Comparator.comparingInt(x -> x.deadline));

            int time = 0;
            for (Process pr : a) {
                time = Math.max(time, pr.at);
                pr.wt = time - pr.at;
                pr.tat = pr.wt + pr.bt;
                time += pr.bt;
            }

            print(a);
        }
    }

    // ------ MULTILEVEL QUEUE (FCFS + SJF) ------
    static void mlq(Process p[]) {
        Process a[] = copy(p);

        System.out.println("Input number of queue:");
        System.out.println("Queue 1: FCFS");
        System.out.println("Queue 2: SJF");

        // Split: even -> FCFS, odd -> SJF
        List<Process> q1 = new ArrayList<>();
        List<Process> q2 = new ArrayList<>();

        for (Process pr : a) {
            if (pr.id % 2 == 1) q1.add(pr);
            else q2.add(pr);
        }

        Process arr1[] = q1.toArray(new Process[0]);
        Process arr2[] = q2.toArray(new Process[0]);

        System.out.println("\n--- Queue 1: FCFS ---");
        fcfs(arr1);

        System.out.println("\n--- Queue 2: SJF ---");
        sjf(arr2);
    }

    // ------------ UTILITIES --------------------

    static Process[] copy(Process p[]) {
        Process a[] = new Process[p.length];
        for (int i = 0; i < p.length; i++) {
            a[i] = new Process();
            a[i].id = p[i].id;
            a[i].at = p[i].at;
            a[i].bt = p[i].bt;
            a[i].prio = p[i].prio;
            a[i].deadline = p[i].deadline;
        }
        return a;
    }

    static void print(Process a[]) {
        double avgWT = 0, avgTAT = 0;

        System.out.println("Waiting time\tTurnaround time:");
        for (Process pr : a) {
            System.out.println("\tP" + pr.id + ": " + pr.wt + "\t\tP" + pr.id + ": " + pr.tat);
            avgWT += pr.wt;
            avgTAT += pr.tat;
        }

        System.out.printf("Average Waiting Time: %.2f\n", avgWT / a.length);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT / a.length);
    }
}
