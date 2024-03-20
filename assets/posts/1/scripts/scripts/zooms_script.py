import os
from typing import Literal, Tuple

from PIL.Image import Image, open

IN_FOLDER = "./out/upscales"
OUT_FOLDER = "./out/zooms"


def zoom(
    image: Image, top_left: Tuple[int, int], bottom_right: Tuple[int, int]
) -> Image:
    return image.crop((top_left[0], top_left[1], bottom_right[0], bottom_right[1]))


if __name__ == "__main__":
    zoom_inputs: dict[
        str, dict[Literal["top_left", "bottom_right"], Tuple[int, int]]
    ] = {
        "abstract": {"top_left": (205, 120), "bottom_right": (280, 160)},
        "animal": {"top_left": (125, 560), "bottom_right": (210, 620)},
        "nature": {"top_left": (305, 335), "bottom_right": (460, 465)},
        "object": {"top_left": (230, 385), "bottom_right": (325, 450)},
        "people": {"top_left": (250, 240), "bottom_right": (320, 290)},
    }
    print("Zoom inputs:", zoom_inputs)

    image_paths = [
        f"{IN_FOLDER}/{x}" for x in os.listdir(IN_FOLDER) if x.endswith(".png")
    ]
    print("Found images:", image_paths)

    for filename_prefix, zoom_input in zoom_inputs.items():
        image_to_zoom_paths = [
            path
            for path in image_paths
            if path.split("/")[-1].startswith(filename_prefix)
        ]
        image_to_zoom_filenames = [
            filename
            for path in image_paths
            if (filename := path.split("/")[-1].split(".")[0])
            and filename.startswith(filename_prefix)
        ]
        images_zoomed = [
            zoom(open(path), zoom_input["top_left"], zoom_input["bottom_right"])
            for path in image_to_zoom_paths
        ]
        for filename, image_zoomed in zip(image_to_zoom_filenames, images_zoomed):
            image_zoomed.save(f"{OUT_FOLDER}/{filename}_zoomed.png")
        print("Processing success images prefixed:", filename_prefix)
