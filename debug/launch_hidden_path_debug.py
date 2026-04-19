import shutil
import os
import sys
from pathlib import Path

root = Path(__file__).resolve().parent.parent
src = root / "src" / "hidden_path.py"
dst = root / "debug" / "hidden_path.py"
dst.parent.mkdir(exist_ok=True)

shutil.copy2(src, dst)
print(f"Copied {src} to {dst}")

if __name__ == "__main__":
    args = sys.argv[1:]
    if "--debug" not in args:
        args.insert(0, "--debug")
    os.execv(sys.executable, [sys.executable, str(dst)] + args)
