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
bb style-check-all  # Check formatting (all directories)
bb lint             # Lint with clj-kondo (src + test)
bb lint-all         # Lint all code (src + test + dev + notebooks)
bb splint           # Check idiomatic style with Splint (src + test)
bb splint-all       # Check idiomatic style (all directories)
bb test             # Run tests via kaocha

# Run a single test (kaocha focus)
bb test --focus brave.notebooks-test/export-config-test

# Development
bb clerk            # Start Clerk notebook server on port 7777
bb dev-repl         # Start Clojure REPL with dev deps on classpath
bb clean            # Remove build artifacts and caches
```

## REPL Workflow with Notebooks

`bb dev-repl` (i.e. `clojure -M:dev`) starts a REPL with `notebooks/` on the classpath. To work with a notebook interactively:

```clojure
;; Load a notebook namespace into the REPL
(require 'ch03-do-things-crash-course :reload)

;; Or switch into it so you can eval forms directly
(in-ns 'ch03-do-things-crash-course)
```

With **editor jack-in** (Cursive, CIDER, Calva), open the notebook file and evaluate forms inline — no manual `require` needed. The editor connects to a REPL started with the `:dev` alias, which already has `notebooks/` on the classpath.

The intended workflow: write code in the notebook file, eval individual forms via the REPL for instant feedback, and save the file for Clerk to render the whole document in the browser.

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

## Logging

SLF4J simple logger is enabled automatically. Use `org.slf4j.LoggerFactory` to create loggers:

```clojure
(import 'org.slf4j.LoggerFactory)
(def log (LoggerFactory/getLogger "demo"))
(.info log "Hello logs")
```
