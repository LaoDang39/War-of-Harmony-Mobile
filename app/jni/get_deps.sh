#!/usr/bin/env bash

# This script downloads/git clones project dependencies
# such as libogg, SDL2, Ruby, etc.

set -euo pipefail

GIT_ARGS=(--filter=blob:none -c advice.detachedHead=false --single-branch --depth 1)

clone_repo() {
  local dir="$1"
  local tag="$2"
  shift 2

  if [[ -d "$dir" ]]; then
    echo "[skip] $dir already exists"
    return 0
  fi

  local url
  for url in "$@"; do
    echo "Downloading $dir from $url..."
    if git clone "${GIT_ARGS[@]}" -b "$tag" "$url" "$dir"; then
      return 0
    fi
    echo "[warn] Failed to clone $url"
  done

  echo "[error] Unable to download $dir from any configured source" >&2
  return 1
}

download_tarball() {
  local dir="$1"
  local archive_name="$2"
  local extract_dir="$3"
  shift 3

  if [[ -d "$dir" ]]; then
    echo "[skip] $dir already exists"
    return 0
  fi

  local url
  for url in "$@"; do
    echo "Downloading $dir from $url..."
    rm -f "$archive_name"
    if wget -q --https-only --show-progress -O "$archive_name" "$url"; then
      tar -xzf "$archive_name"
      mv "$extract_dir" "$dir"
      rm -f "$archive_name"
      return 0
    fi
    echo "[warn] Failed to download $url"
  done

  rm -f "$archive_name"
  echo "[error] Unable to download tarball for $dir from any configured source" >&2
  return 1
}

# Xiph libogg
clone_repo libogg v1.3.5 \
  https://github.com/xiph/ogg

# Xiph libvorbis
clone_repo libvorbis v1.3.7 \
  https://github.com/xiph/vorbis

# Xiph libtheora
clone_repo libtheora v1.1.1 \
  https://github.com/xiph/theora \
  https://gitlab.xiph.org/xiph/theora.git

# GNU libiconv
download_tarball libiconv libiconv-1.17.tar.gz libiconv-1.17 \
  https://ftp.gnu.org/pub/gnu/libiconv/libiconv-1.17.tar.gz \
  https://mirrors.kernel.org/gnu/libiconv/libiconv-1.17.tar.gz

# Freedesktop uchardet (FIXED: GitHub 不存在，换为 GitLab 官方源)
clone_repo uchardet v0.0.8 \
  https://gitlab.freedesktop.org/uchardet/uchardet \
  https://github.com/oe-mirrors/uchardet

# Freedesktop Pixman (FIXED: GitHub 不存在，换为 GitLab 官方源)
clone_repo pixman pixman-0.42.2 \
  https://gitlab.freedesktop.org/pixman/pixman \
  https://github.com/libpixman/pixman

# PhysicsFS
clone_repo physfs release-3.2.0 \
  https://github.com/icculus/physfs

# OpenAL Soft 1.23.0
clone_repo openal 1.23.0 \
  https://github.com/kcat/openal-soft

# SDL2
clone_repo SDL2 release-2.26.3 \
  https://github.com/libsdl-org/SDL

# SDL2_image
if [[ ! -d "SDL2_image" ]]; then
  echo "Downloading SDL2_image..."
  git clone "${GIT_ARGS[@]}" --recurse-submodules -b release-2.6.3 \
    https://github.com/libsdl-org/SDL_image SDL2_image
else
  echo "[skip] SDL2_image already exists"
fi

# SDL2_ttf
if [[ ! -d "SDL2_ttf" ]]; then
  echo "Downloading SDL2_ttf..."
  git clone "${GIT_ARGS[@]}" --recurse-submodules -b release-2.20.2 \
    https://github.com/libsdl-org/SDL_ttf SDL2_ttf
else
  echo "[skip] SDL2_ttf already exists"
fi

# SDL2_sound
clone_repo SDL2_sound v2.0.1 \
  https://github.com/icculus/SDL_sound

# OpenSSL 1.1.1t
clone_repo openssl OpenSSL_1_1_1t \
  https://github.com/openssl/openssl

# Ruby 3.1.0 (patched for mkxp-z)
# 注：mkxp-z/ruby 若无法访问，可改用官方 Ruby 源（需将 tag 改为 v3_1_0）：
#   clone_repo ruby v3_1_0 https://github.com/ruby/ruby
clone_repo ruby mkxp-z-3.1 \
  https://github.com/mkxp-z/ruby \
  https://github.com/ruby/ruby

echo "Done!"
