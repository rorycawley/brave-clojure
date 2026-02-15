# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Clojure starter/template project built around executable Clerk notebooks as living documentation. Uses `mise` for reproducible toolchain management and Babashka for task running.

## Toolchain

Uses `mise` to pin tool versions (Java 21, Babashka, clj-kondo, cljstyle). Run `mise install` first to bootstrap. Shell has `mise activate` configured, so `bb` commands work directly without `mise exec --` prefix.

## Common Commands

```bash
# Quality gates (run in order: style → lint → splint → test)
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
bb test --focus brave.smoke-test/smoke

# Development
bb clerk            # Start Clerk notebook server on port 7777
bb dev-repl         # Start nREPL with dev dependencies
bb clean            # Remove build artifacts and caches
```


## Development Workflow

A typical session has two things running side by side: a **REPL** (for evaluating code) and **Clerk** (for rendering notebooks in the browser). You write code in notebook files — they are normal `.clj` files that Clerk also knows how to render.

### Starting a session

1. **Start Clerk** — run `bb clerk` in a terminal. This watches `notebooks/` and serves on `http://localhost:7777`. Keep this running throughout the session.
2. **Start REPL** — use IntelliJ/Cursive's jack-in (with the `:dev` alias). This connects the editor to a running Clojure process so you can evaluate code from any file.

### Writing code

3. **Open a notebook** — open a file in `notebooks/` (e.g. `ch00_template.clj`) in IntelliJ. This is a regular Clojure file. You write code here.
4. **Evaluate with the REPL** — use Cursive's shortcuts to send forms to the REPL (e.g. Ctrl+Enter for a single form). This is how you test code as you write — you get instant feedback in the editor.
5. **See rendered output in Clerk** — when you save the file, Clerk auto-reloads it in the browser at `localhost:7777`. Clerk shows the results of each form plus any `clerk/md` prose, formatted as a readable document.

So the loop is: **write code in the notebook file, evaluate it via REPL to test, save to see it rendered in Clerk**.

### Graduating code to src

6. **Promote to src** — once code stabilises, extract functions into `src/brave/`. The notebook then `require`s and calls those functions, becoming documentation rather than scratch code.
7. **Add tests** — write tests in `test/brave/` for any code promoted to `src/`.
8. **Quality check** — run `bb check` (style + lint + splint + test) before committing.

### Adding a new chapter

1. Copy `notebooks/ch00_template.clj` to `notebooks/chNN_topic.clj`.
2. Update the namespace to match: `(ns chNN-topic ...)`.
3. Add the path to `dev/export_clerk.clj` so it's included in static builds.
4. Clerk auto-discovers it via the `notebooks/` watch path.

## Architecture

- **src/brave/** — Production code. Namespaces follow `brave.*` convention.
- **test/brave/** — Tests using `clojure.test`, run by kaocha. Test namespaces use `brave.*-test`.
- **dev/** — REPL helpers: `user.clj` (tools.namespace refresh), `clerk.clj` (notebook server), `export_clerk.clj` (static HTML export).
- **notebooks/** — Clerk notebooks (the primary output). `dashboard.clj` auto-discovers and links chapters. New chapters follow `ch00_template.clj` pattern.

## Code Quality

- **Formatting**: cljstyle with `:community` style. Run `bb fmt` before committing.
- **Linting**: clj-kondo. The `src/` directory enforces `:missing-docstring` warnings. Separate configs per directory in `.clj-kondo/{src,dev,notebooks}/config.edn`.
- **Idiomatic style**: [Splint](https://github.com/NoahTheDuke/splint) checks for non-idiomatic patterns (e.g. `(+ x 1)` → `(inc x)`). Runs automatically as part of `bb check`.
- **Tests**: Kaocha configured in `tests.edn`. Test paths: `test/`.

## CI/CD

GitHub Actions runs `bb build` on push to main and PRs. A separate workflow exports Clerk notebooks and publishes to GitHub Pages.