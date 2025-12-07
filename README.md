# CPU Scheduling Algorithm Simulator

CPU Scheduling Algorithm Simulator — a self-contained front-end tool (single HTML file) for visualizing and analyzing non-preemptive CPU scheduling algorithms (plus Round Robin and a Multilevel Queue variant). This simulator is intended for teaching, experimentation, and assignments in Operating Systems courses.

Project snapshot

- Title: CPU Scheduling Algorithm Simulator
- File: `nonPreempt_CPUsched.html`
- Purpose: Input a set of processes and visualize scheduling results (Gantt chart) and per-process metrics (Completion, Turnaround, Waiting times).
- Target audience: Students learning CPU scheduling algorithms.

Table of contents

1. Project overview
2. Features
3. File layout
4. Algorithms implemented (behavioral summary)
5. Inputs and UI controls
6. How results are calculated
7. How to run locally
8. Example usage
9. Known limitations & assumptions
10. Troubleshooting & tips
11. Development notes and suggestions for improvement
12. License

1. Project overview
This is a single-page HTML app with embedded CSS and JavaScript. It allows the user to:

- Define number of processes (2–9).
- Set process parameters: Arrival Time, Burst Time, Priority, Deadline, Queue (for MLQ).
- Choose an algorithm: FCFS, SJF, Priority (non-preemptive), Deadline (shortest deadline first), MLQ (multilevel queue with FCFS/SJF/RR subqueues), Round Robin (RR).
- Run a simulation to produce:
  - A visual Gantt chart with labeled segments and time marks.
  - A metrics table listing CT, TAT, WT for each process.
  - Aggregate metrics (Average Waiting Time, Average Turnaround Time, Total Processes, Selected Algorithm).

2. Features

- Clean, responsive UI with a modern card-based layout.
- Dynamic process input generation based on number of processes.
- Per-algorithm field requirements: only relevant inputs become required/active.
- Visual Gantt chart showing process segments and idle periods.
- Time label rendering aligned to segment start/end values.
- Multilevel Queue (MLQ) combining FCFS (Q1), SJF (Q2), and RR (Q3).
- Round Robin support with configurable time quantum.

3. File layout

- nonPreempt_CPUsched.html — complete app: HTML, CSS, JS (single file).
- README.md (this file) — documentation.

4. Algorithms implemented (behavioral summary)

- FCFS (First Come First Serve)
  - Sorts by arrival time (ties: process id) and executes each to completion.
  - Inserts idle segments when CPU is idle until the next arrival.

- SJF (Shortest Job First — non-preemptive)
  - At each scheduling decision, chooses the available job with the smallest burst time.
  - If none are available, advances to the next arrival and adds idle time.

- Priority (Non-preemptive)
  - Lower numeric priority value = higher scheduling priority (1 highest).
  - Between tied priorities, breaks ties by arrival time then id.

- Deadline (Shortest Deadline First)
  - Chooses among available jobs the one with the closest (smallest) deadline.
  - Ties resolved by arrival time then id.

- MLQ (Multilevel Queue)
  - Q1 (highest): FCFS — execute to completion.
  - Q2 (mid): SJF — select shortest job among available.
  - Q3 (lowest): RR — round-robin with configured time quantum (processes maintain remaining burst).
  - Scheduler checks queues in priority order each scheduling cycle.

- RR (Round Robin)
  - Classic round-robin with a configurable time quantum.
  - Handles process arrivals during execution by enqueueing arriving processes.
  - Maintains per-process remaining burst time and re-enqueues unfinished processes.

5. Inputs and UI controls

- Number of Processes: integer (2–9).
- For each process:
  - Arrival Time (non-negative integer).
  - Burst Time (positive integer).
  - Priority (1–10, 1 highest). Required for Priority or optional depending on algorithm.
  - Deadline (non-negative integer). Required for Deadline algorithm.
  - Queue (1=FCFS, 2=SJF, 3=RR). Required for MLQ.
- Time Quantum: for RR and MLQ(RR queue). Default = 2 (configurable).
- Run Simulation button: calculates scheduling and shows results.

6. How results are calculated (implementation notes)

- Each algorithm returns:
  - results: array of per-process objects with computed ct (completion time), tat (turnaround time), wt (waiting time).
  - gantt: array of segments with { id, start, end, type } where type is 'process' or 'idle'.
- Turnaround Time (TAT) = Completion Time (CT) − Arrival Time (AT).
- Waiting Time (WT) = TAT − Burst Time (BT).
- Gantt chart rendering:
  - Segments are sized proportionally to their duration vs finalTime.
  - A set of colors is used for process segments; idle segments use gray.
  - Time labels are placed at each unique start/end time point.
- Input validation:
  - Burst time must be > 0 for each process (enforced at simulation time).
  - Required fields depend on selected algorithm (UI toggles required attributes).

7. How to run locally
Option A — Open directly (simple)

- Double-click/open `nonPreempt_CPUsched.html` in a modern browser (Chrome, Firefox, Edge).
- Note: some browsers restrict file:// scripts; if interactive behavior fails, use Option B.

Option B — Serve over a simple HTTP server (recommended)

- Using Python 3 (recommended):
  - Open a terminal in the directory containing `nonPreempt_CPUsched.html`.
  - Run: `python -m http.server 8000`
  - Open: <http://localhost:8000/nonPreempt_CPUsched.html>
- Using Node (http-server):
  - `npm install -g http-server`
  - `http-server -p 8000`
  - Open: <http://localhost:8000/nonPreempt_CPUsched.html>

8. Example usage

- Start with 3 processes (default).
- Example process set (default randomized values are seeded when inputs are generated).
- Select algorithm "SJF".
- Click "Run Simulation".
- Expected: Gantt chart showing chosen ordering, table with CT/TAT/WT, and average metrics updated.

9. Known limitations & assumptions

- Non-preemptive assumption for FCFS, SJF, Priority, and Deadline (they run each chosen process to completion).
- SJF implemented as non-preemptive SJF (i.e., no preemption if a shorter job arrives while one is executing).
- MLQ's RR queue uses simple RR behavior — fairness depends on queue ordering; detailed quantum/aging policies are not implemented.
- Time units are integers. The UI may display fractional TAT/WT due to division when computing averages; individual TAT/WT are computed as integers (but formatted with two decimals in the UI).
- No persistence: process inputs are not saved across page reloads.
- No accessibility features beyond basic semantic markup; screen-reader support is limited.
- No backend: purely client-side; no data export implemented.
- The app uses randomized initial values for burst/prio/deadline when re-generating processes; always double-check fields before running.

10. Troubleshooting & tips

- If the "Run Simulation" button does nothing:
  - Open the browser console (F12) and check for JS errors.
  - Ensure required fields are enabled/filled for the selected algorithm (some inputs are disabled for algorithms that don't need them).
- If Gantt chart appears empty:
  - Confirm that results array returned from scheduler is non-empty and the finalTime computed is > 0.
- If fields appear disabled unexpectedly:
  - Re-select the desired algorithm card to refresh required/disabled logic.
- When testing RR or MLQ, ensure `Time Quantum` is visible (RR and MLQ show the quantum input). If hidden, re-select the RR/MLQ algorithm.

11. Development notes and suggestions for improvement

- Unit tests: extract scheduling functions to separate JS modules and add unit tests (jest or similar).
- Persist inputs: add localStorage support to keep process definitions between sessions.
- Export/Import: allow exporting a process set as JSON and importing it to share scenarios.
- Accessibility: improve labels, ARIA roles, keyboard navigation.
- More metrics: add CPU utilization, throughput, context switches (for preemptive variants).
- Preemptive options: implement preemptive SJF (SRTF) and preemptive priority scheduling.
- Performance: for many processes or very large times, consider normalizing timeline or zooming features.
- Color and styling: make Gantt color palette configurable and add legend mapping colors to processes.
- Add process re-ordering and drag-drop to adjust arrival times visually.

12. License

- Default: MIT-style permissive license recommended. Add a `LICENSE` file if you want explicit terms.

Contributing

- Open an issue or submit a pull request with a clear description and test scenarios.
- Keep changes modular: extract algorithm implementations to named functions and add unit tests.

Contact / Attribution

- This file documents the single-file front-end simulator included in the repository. Use responsibly for educational and demonstration purposes.

End of README
