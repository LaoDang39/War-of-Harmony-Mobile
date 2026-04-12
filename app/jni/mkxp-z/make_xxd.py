from __future__ import annotations

import pathlib


ROOT = pathlib.Path(__file__).resolve().parent


def sanitize(stem: str) -> str:
    return "".join(ch if ch.isalnum() else "_" for ch in stem)


def emit_xxd(src_dir: pathlib.Path, out_dir: pathlib.Path, prefix: str) -> None:
    out_dir.mkdir(parents=True, exist_ok=True)
    for old in out_dir.glob("*.xxd"):
        old.unlink()

    for src in sorted(path for path in src_dir.iterdir() if path.is_file()):
        data = src.read_bytes()
        symbol = f"{prefix}_{sanitize(src.name)}"
        out_path = out_dir / f"{src.name}.xxd"

        lines = [f"extern const unsigned char {symbol}[] = {{"]
        for i in range(0, len(data), 16):
            chunk = data[i:i + 16]
            suffix = "," if i + 16 < len(data) else ""
            lines.append("  " + ", ".join(f"0x{b:02x}" for b in chunk) + suffix)
        lines.append("};")
        lines.append(f"extern const unsigned int {symbol}_len = {len(data)};")

        out_path.write_text("\n".join(lines) + "\n", encoding="ascii")


def main() -> None:
    emit_xxd(ROOT / "assets", ROOT / "xxd" / "assets", "assets")
    emit_xxd(ROOT / "shader", ROOT / "xxd" / "shader", "shader")


if __name__ == "__main__":
    main()
