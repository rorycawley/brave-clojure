# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Clojure project built around executable Clerk notebooks as living documentation for *Clojure for the Brave and True*. Uses `mise` for reproducible toolchain management and Babashka for task running. See `README.md` for full developer onboarding.

## Toolchain

Uses `mise` to pin tool versions (Java 21, Clojure, Babashka, clj-kondo, cljstyle). Run `mise install` first to bootstrap. Shell has `mise activate` configured, so `bb` commands work directly without `mise exec --` prefix.

## Common Commands

```bash
# Quality gates (run in order: style-check-all → lint-all → splint → test)
bb check

# Full build including Clerk notebook export
bb build

# Individual steps
bb fmt              # Auto-fix formatting (cljstyle)
bb style-check      # Check formatting (src + test only)
bb lint             # Lint with clj-kondo (src + test)
bb splint           # Check idiomatic style with Splint (src + test)
bb test             # Run tests via kaocha

# Run a single test (kaocha focus)
bb test --focus brave.notebooks-test/export-config-test

# Development
bb clerk            # Start Clerk notebook server on port 7777
bb dev-repl         # Start Clojure REPL with dev deps on classpath
bb clean            # Remove build artifacts and caches
```

## Architecture

- **src/brave/** — Production code. Namespaces follow `brave.*` convention.
- **test/brave/** — Tests using `clojure.test`, run by kaocha. Test namespaces use `brave.*-test`.
- **dev/** — REPL helpers: `user.clj` (tools.namespace refresh), `clerk.clj` (notebook server), `export_clerk.clj` (static HTML export).
- **notebooks/** — Clerk notebooks (the primary output): one file per book chapter plus `index.clj` as table of contents.

## Conventions

- One notebook per chapter: `notebooks/chNN_topic.clj` (`NN` from `01` to `13`).
- `notebooks/index.clj` is the table-of-contents notebook.
- Export auto-discovers chapter files by filename pattern; no manual path list needed.
- Stabilised code goes to `src/brave/`, with tests in `test/brave/`.
- Run `bb check` before committing.

## Code Quality

- **Formatting**: cljstyle with `:community` style. Run `bb fmt` before committing.
- **Linting**: clj-kondo. The `src/` directory enforces `:missing-docstring` warnings. Separate configs per directory in `.clj-kondo/{src,dev,notebooks}/config.edn`.
- **Idiomatic style**: [Splint](https://github.com/NoahTheDuke/splint) checks for non-idiomatic patterns (e.g. `(+ x 1)` → `(inc x)`). Runs automatically as part of `bb check`.
- **Tests**: Kaocha configured in `tests.edn`. Test paths: `test/`.

## CI/CD

GitHub Actions runs `bb check` on push to main and PRs. A separate workflow runs `bb clerk-export` and publishes to GitHub Pages.
