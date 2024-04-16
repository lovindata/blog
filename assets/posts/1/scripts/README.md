# Scripts

## Contribution

Please install [Python](https://www.python.org/downloads/).

Please install [Poetry](https://python-poetry.org/docs/#installing-with-the-official-installer) with the official installer.

Please install [VSCode](https://code.visualstudio.com/) and its extensions:

- Black Formatter
- isort
- Python
- Pylance
- Even Better TOML
- Prettier

If you need to switch between Python interpreters:

- `CTRL + SHIFT + P`
- `Python: Select Interpreter`

To have your Python environment inside your project (optional):

```bash
poetry config virtualenvs.in-project true
```

To create your Python environment and install dependencies:

```bash
poetry install
```

To update dependencies:

```bash
poetry update
```

To clear poetry cache:

```bash
poetry cache clear --all .
```
