---
title: "Image Super-Resolution: DAT (Dual Aggregation Transformer) the new goat for Pythonistas? 🐍"
date: 2024-02-21
categories:
  - Python
  - Library
---

As a Python aficionado, I sought to leverage my programming acumen to enhance cherished images. Armed with the powerful **Pillow** library, I embarked on this mission, only to be met with disappointment 😑 when the results from the conventional **resize** method yielded subpar quality – a far cry from the vibrant images I envisioned. In this article, I am excited to unveil a groundbreaking solution. Say goodbye to lackluster results and hello to superior image quality! 🫨

<!-- more -->

![DAT light VS Bicubic](image.png)

## 🤔 Why DAT?

Image super resolution and bicubic interpolation differ in their methods for improving image quality. Bicubic interpolation is a simple algorithm that adjusts pixel values to resize images, often leading to blurry outcomes when enlarging. Conversely, image super resolution, powered by deep learning models like CNNs, aims to create high-resolution images with enhanced details by learning from high-quality data.

**DAT** stands as one of the foremost image super-resolution algorithms. Presently, accessible and user-friendly open-source alternatives encompass:

- **OpenCV's** models (**EDSR, ESPCN, FSRCNN, LapSRN**) from [`opencv-python-headless-contrib`](https://pypi.org/project/opencv-contrib-python-headless/) library
- **ESRGAN** from [github.com/xinntao/ESRGAN](https://github.com/xinntao/ESRGAN)

These options necessitate an initial setup, like downloading weights. For ESRGAN, some code tinkering is needed to make it programmatically usable. Additionally, these alternatives are becoming dated — **EDSR** was released in 2017 and **ESRGAN** in 2018. The field of research in image super resolution has since progressed.

<figure markdown="span">
  ![Image Super-Resolution on Set14 - 4x upscaling](image-1.png)
  <figcaption>Image Super-Resolution on Set14 - 4x upscaling (paperswithcode.com)</figcaption>
</figure>

As machine learning engineers, data scientists, or even Python developers, we always seek the best, newest, most versatile, user-friendly, and fastest models, don't we? So, let's kick things off with [**DAT**](https://pypi.org/project/pillow-dat/)! 😎

## 🚀 How to use DAT?

### Quickstart: just `upscale`

For the installation according to your dependency manager:

```bash title="PyPI"
pip install pillow-dat
```

```bash title="Poetry"
poetry add pillow-dat
```

For the usage:

```python title="example.py" hl_lines="6"
from PIL.Image import open

from PIL_DAT.Image import upscale

lumine_image = open(".github/lumine.png")
lumine_image = upscale(lumine_image, 2)
```

As you can see, **DAT** is usable through [`pillow-dat`](https://pypi.org/project/pillow-dat/), a **Pillow** library extension. It provides an upscale of times 2, 3 and 4. But the best of all, it's just one line of code 😎.

### Advanced: **custom models**

The library offers four versions of **DAT** models for advanced programmers. Here's an example for a scaling factor of 4:

- **DAT light** with **573K parameters**
- **DAT S** with **11.21M parameters**
- **DAT 2** with **11.21M parameters**
- **DAT** with **14.80M parameters**

By default, the method `PIL_DAT.Image.upscale` utilizes the **DAT light** models. However, if you're feeling adventurous or require even higher image quality, you can access these advanced versions using **custom models**:

```python title="example_custom_model.py" hl_lines="6-7"
from PIL.Image import open

from PIL_DAT.dat_s import DATS

lumine_image = open(".github/lumine.png")
model = DATS("./dist/DAT_S_x4.pth", 4)  # Instantiate a reusable custom model instance
lumine_image = model.upscale(lumine_image)
lumine_image.show()
```

Please note that model weights in `*.pth` format are accessible via a Google Drive link provided on [GitHub](https://github.com/lovindata/pillow-dat) or [PyPI](https://pypi.org/project/pillow-dat/).

By default, when you use the `PIL_DAT.Image.upscale` method, it loads the model, performs the upscaling, and clears the model from the RAM for you. For better performance, especially when calling this function multiple times for the same scaling factor, it's recommended to instantiate the **DAT light** model via **custom models**.

```python
from PIL.Image import open

from PIL_DAT.dat_light import DATLight

lumine_image = open(".github/lumine.png")
model = DATLight(2)  # Instantiate a reusable DATLight custom model instance
lumine_image = model.upscale(lumine_image)
lumine_image.show()
```

## 📊 Benchmarks

**DAT** will be compared to **OpenCV's** top super resolution model, **EDSR**, or a commercial SaaS product, [**Img.Upscaler**](https://imgupscaler.com/).

All benchmark results presented here are reproducible. For detailed implementation, please refer to the following files in the [benchmarks](https://github.com/lovindata/pillow-dat/tree/main/benchmarks) folder.

### Speed

Performance benchmarks have been conducted on a computing system equipped with an **Intel(R) CORE(TM) i7-9750H CPU @ 2.60GHz processor**, accompanied by a **2 × 8 Go at 2667MHz RAM** configuration. Below are the recorded results:

|  _In seconds_  | 320 × 320 | 640 × 640 | 960 × 960 | 1280 × 1280 |
| :------------: | :-------: | :-------: | :-------: | :---------: |
| DAT light (x2) |   16.1    |   65.3    |   146.8   |    339.8    |
| DAT light (x3) |   14.3    |   61.7    |     -     |      -      |
| DAT light (x4) |   14.0    |   63.0    |     -     |      -      |

The results were compared against the renowned [`OpenCV`](https://opencv.org/) library, utilizing its `EDSR` model known for delivering superior image quality.

| _In seconds_ | 320 × 320 | 640 × 640 | 960 × 960 | 1280 × 1280 |
| :----------: | :-------: | :-------: | :-------: | :---------: |
|  EDSR (x2)   |   25.6    |   112.9   |   264.1   |    472.8    |
|  EDSR (x3)   |   24.3    |   112.5   |     -     |      -      |
|  EDSR (x4)   |   23.6    |   111.2   |     -     |      -      |

_Note:_ Since we don't have control over [**Img.Upscaler's**](https://imgupscaler.com/) hardware specifications, it's challenging to provide accurate speed benchmarks.

### Quality

#### Nature

#### People

#### Objects

#### Animals

#### Abstract

### Alpha-channel-awareness

State-of-the-art super-resolution models typically only support RGB images, and this holds true for the original **DAT** models as well. The reason behind this is that datasets used for comparing models in research predominantly consist of RGB images. As a user, this can pose a challenge. However, [`pillow-dat`](https://github.com/lovindata/pillow-dat) offers a solution with its **built-in post-processing logic, effortlessly handling any alpha channel** for you! 🌟

<figure markdown="span">
  ![Alpha-channel-awareness](image-2.png)
  <figcaption>Alpha-channel-awareness</figcaption>
</figure>

You can manage the alpha channel using **OpenCV's EDSR** by creating your own post-processing logic. In this example, we simply compare the raw usage of each solution.

## 👑 Acknowledgment to the researcher!
