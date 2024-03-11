from backend.conf.fastapi_conf import run


def dev() -> None:
    print("coucou")
    run()


def start() -> None:
    print("prod coucou")
    run()
