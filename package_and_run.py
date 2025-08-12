import os
import shutil
import subprocess

# Configuration
jar_name = "ForrestGame.jar"
exported_jar_path = f"./{jar_name}"  # assumes it's in the root directory
build_dir = "build"
dist_dir = "dist-mac"
main_class = "Runner"
app_name = "ForrestGame"
icon_path = os.path.join(build_dir, "icon.icns")  # Your specified icon path

# 1. Delete previous dist directory
if os.path.exists(dist_dir):
    print(f"Removing old {dist_dir}/ directory...")
    shutil.rmtree(dist_dir)

# 2. Move exported JAR to build/ directory
dest_jar_path = os.path.join(build_dir, jar_name)
if os.path.exists(exported_jar_path):
    print(f"Moving {jar_name} to {build_dir}/...")
    shutil.move(exported_jar_path, dest_jar_path)
else:
    print(f"ERROR: {jar_name} not found in root directory.")
    exit(1)

# 3. Run jpackage with icon support
print("Running jpackage...")
jpackage_cmd = [
    "jpackage",
    "--type", "app-image",
    "--input", build_dir,
    "--name", app_name,
    "--main-jar", jar_name,
    "--main-class", main_class,
    "--dest", dist_dir,
    "--java-options", "-Xmx512m",
]

# Add icon if it exists
if os.path.exists(icon_path):
    jpackage_cmd += ["--icon", icon_path]
else:
    print("WARNING: Icon not found, proceeding without it.")

try:
    subprocess.run(jpackage_cmd, check=True)
except subprocess.CalledProcessError:
    print("ERROR: jpackage failed.")
    exit(1)

# 4. Run the app binary directly (so console output appears)
binary_path = os.path.join(dist_dir, f"{app_name}.app", "Contents", "MacOS", app_name)
if os.path.exists(binary_path):
    print("Launching app...")
    subprocess.run([binary_path])
else:
    print(f"ERROR: Could not find binary at {binary_path}")
