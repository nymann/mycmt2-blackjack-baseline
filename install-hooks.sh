#!/usr/bin/env bash
set -euo pipefail

HOOK_DIR="$(git rev-parse --show-toplevel)/.git/hooks"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SOURCE="$SCRIPT_DIR/hooks/pre-commit"

if [ ! -f "$SOURCE" ]; then
    echo "Error: hooks/pre-commit not found" >&2
    exit 1
fi

cp "$SOURCE" "$HOOK_DIR/pre-commit"
chmod +x "$HOOK_DIR/pre-commit"
echo "Installed pre-commit hook."
