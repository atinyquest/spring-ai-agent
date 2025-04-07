#!/bin/bash

OS="$(uname -s)"

if [[ "$OS" == "Linux" || "$OS" == "Darwin" ]]; then
  echo "🔧 Git Hook 설정 중... (OS: $OS)"
  cp ./hooks/commit-msg .git/hooks/commit-msg
  cp ./hooks/pre-push .git/hooks/pre-push
  chmod +x .git/hooks/*
  echo "✅ Git Hook 설정 완료!"
else
  echo "❌ 이 스크립트는 Linux/Mac에서만 실행됩니다."
  echo "👉 Windows 사용자는 Git Bash에서 실행하세요: sh scripts/init.sh"
  exit 1
fi
