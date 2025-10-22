# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Repository type: Java Swing desktop app with MySQL backend (no Maven/Gradle). GUI built with NetBeans GUI Builder (.form files).

Commands (build, run, DB)
- Initialize DB schema and seed data
  - Requires a local MySQL instance. Import the provided schema and seed:
    ```bash
    mysql -u {{DB_USER}} -p -h localhost < Database/hms.sql
    ```

- Compile from CLI (uses the MySQL JDBC driver)
  - Place mysql-connector-j-<version>.jar under lib/ (or adjust the path accordingly):
    ```bash
    mkdir -p bin
    javac -cp "lib/mysql-connector-j-<version>.jar" -d bin $(find src -name '*.java')
    ```

- Run the app entry point (Login screen)
  - LoginPage supports env vars DB_USER and DB_PASS (defaults shown in code). Other screens hardcode DB creds; see Architecture notes.
    ```bash
    export DB_USER={{DB_USER}}
    export DB_PASS={{DB_PASS}}
    java -cp "bin:lib/mysql-connector-j-<version>.jar" hospital.LoginPage
    ```
  - macOS/Linux classpath separator is ":". On Windows, use ";" instead of ":".

- Run a specific screen directly (each has a main method)
  ```bash
  java -cp "bin:lib/mysql-connector-j-<version>.jar" hospital.DOCTORS
  java -cp "bin:lib/mysql-connector-j-<version>.jar" hospital.PATIENT
  ```

- Lint and tests
  - No linter or test suite is configured in this repo.

- IDE usage (matches README)
  - Open the project in NetBeans (or any IDE with Swing support), build, and run hospital.LoginPage.

High-level architecture and structure
- UI-first, event-driven Swing frames
  - Primary screens: LoginPage → welcome → DOCTORS and PATIENT.
  - CRUD flows are split into dedicated frames:
    - Doctors: addDoctor, editDoctor, fireDoctor, viewdetailDoc
    - Patients: admitPatient, editPatient, dischargePatient, viewrecordsPatient
  - Navigation transitions create the target JFrame and dispose the current one.

- Database access pattern (JDBC, inline SQL)
  - Direct JDBC calls in button handlers; repeated connection setup per action:
    - Driver: com.mysql.cj.jdbc.Driver
    - URL: jdbc:mysql://localhost:3306/hms
  - Credentials usage is inconsistent:
    - LoginPage reads DB_USER/DB_PASS from env with defaults.
    - Most other screens hardcode username "root" and password "zahid".
  - SQL is issued directly via PreparedStatement/Statement in each form. There is no DAO/repository layer.

- Data model (from Database/hms.sql)
  - user_login(id, username, password)
  - doctor_record(id, DoctorName, Specialization)
  - patient_record(id, Name, Disease, Date)

- Resources and packaging
  - Icons live under src/icons and are loaded via classpath resources (e.g., getResource("/icons/login.png")). Ensure icons remain on the runtime classpath along with compiled classes.

- Generated UI metadata
  - NetBeans .form files sit beside each .java class and define GUI layouts. Keep .form/.java pairs together; manual edits to generated code blocks may be overwritten by the GUI builder.
