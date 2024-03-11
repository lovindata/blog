import uvicorn
from fastapi import FastAPI


def run() -> None:
    uvicorn.run(
        app=_,
        host="localhost",
        port=8000,
        log_level="info",
        access_log=False,
        reload=True,
        reload_dirs="./backend",
    )


_ = FastAPI()
