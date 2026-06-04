import os
from PIL import Image, ImageDraw

os.makedirs('images', exist_ok=True)
width, height = 1200, 300
img = Image.new('RGB', (width, height), '#dceaf8')
draw = ImageDraw.Draw(img)
for i in range(height):
    ratio = i / height
    r = int(40 + 120 * ratio)
    g = int(110 + 90 * ratio)
    b = int(140 + 70 * ratio)
    draw.line((0, i, width, i), fill=(r, g, b))

# water
for y in range(180, 250):
    draw.line((0, y, width, y), fill=(70, 100, 125))

# dock planks
for x in range(250, 950):
    draw.line((x, 190, x, 250), fill=(115, 74, 41))

for x in range(320, 870, 70):
    draw.rectangle([x, 250, x+20, 275], fill=(103, 61, 31))

# chairs
draw.polygon([(520, 170), (500, 225), (540, 225)], fill=(117, 69, 42))
draw.polygon([(580, 170), (560, 225), (600, 225)], fill=(117, 69, 42))

# sun
draw.ellipse((920, 20, 980, 80), fill=(255, 238, 132))

img.save('images/banner.jpg', quality=85)
print('created images/banner.jpg')
