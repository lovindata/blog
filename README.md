# Blogs

## Installation

Please install [Miniconda](https://docs.conda.io/projects/miniconda/en/latest/index.html).

To install dependencies on a given `blog` Python environnement, run the command:

```bash
conda env create --file environment.yml
```

To update this environment:

```bash
conda env update --file environment.yml --prune
```

To initialize a new blog, run the command:

```bash
& D:\prog\miniconda\envs\blogs\Scripts\mkdocs.exe new .
```

To serve the blog, run the command:

```bash
& D:\prog\miniconda\envs\blogs\Scripts\mkdocs.exe serve
```

_Remark_: Please adapt the paths to your Python setup.
