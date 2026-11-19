#!/usr/bin/env bash
# Starts Keycloak via podman with admin user and the imported realm "coupon".
set -euo pipefail

DIR="$(cd "$(dirname "$0")" && pwd)"
NAME="${KEYCLOAK_CONTAINER:-coupon-keycloak}"
IMAGE="${KEYCLOAK_IMAGE:-quay.io/keycloak/keycloak:26.0}"
PORT="${KEYCLOAK_PORT:-8180}"

if ! podman info >/dev/null 2>&1; then
  if [[ "$(uname)" == "Darwin" ]]; then
    echo "Podman not reachable, starting podman machine..."
    podman machine list --format '{{.Name}}' 2>/dev/null | grep -q . || podman machine init
    podman machine start || true
  fi
  if ! podman info >/dev/null 2>&1; then
    echo "Cannot connect to Podman. Check 'podman system connection list' or run 'podman machine init' and 'podman machine start'." >&2
    exit 1
  fi
fi

podman rm -f "$NAME" >/dev/null 2>&1 || true

podman run -d --name "$NAME" \
  -p "${PORT}:8080" \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  -v "$DIR/coupon-realm.json:/opt/keycloak/data/import/coupon-realm.json:ro,Z" \
  "$IMAGE" start-dev --import-realm

echo "Waiting for realm 'coupon'..."
until curl -sf "http://localhost:${PORT}/realms/coupon/.well-known/openid-configuration" >/dev/null; do
  sleep 2
done

cat <<INFO
Keycloak ready: http://localhost:${PORT}
  Admin console: admin / admin
  Realm:         coupon
  Client:        coupon-client / coupon-client-secret
  User:          coupon-admin / admin   (role coupon_admin)

Get a token:
  curl -s -d grant_type=client_credentials -d client_id=coupon-client \\
    -d client_secret=coupon-client-secret \\
    http://localhost:${PORT}/realms/coupon/protocol/openid-connect/token
INFO
