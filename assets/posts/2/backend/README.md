# Backend

## Contribution

Please install [Java 17](https://adoptium.net/temurin/releases/?version=17).

Please install [VSCode](https://code.visualstudio.com/) and its extensions:

- Metals
- SQLTools
- SQLTools PostgreSQL/Cockroach Driver
- Prettier

The command below assumes that your **Scala project** is located **in the root folder of the open workspace** or **in the root of a dedicated VSCode workspace**.

To setup your Scala environment and install dependencies:

- `CTRL + SHIFT + P`
- `Metals: Import build`

_Remark: Can also be done via Metals UI directly._

To start the backend server:

- `CTRL + SHIFT + P`
- `Metals: Run main class or tests in the current file`

_Remark: Can also be done via the 'Run' label on the main runnable class._

## Database management

To create a new connection for an existing database:

- `CTRL + SHIFT + P`
- `SQLTools Management: Add New Connection`
- Then just follow the instructions

_Remark: Can also be done via SQLTools UI directly._

To connect to a registered connection:

- `CTRL + SHIFT + P`
- `SQLTools: Connect`
- Then just follow the instructions

_Remark: Can also be done via SQLTools UI directly._
