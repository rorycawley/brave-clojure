# brave-clojure

A reproducible Clojure development workflow with:

* **mise** — pinned toolchain (Java, Clojure, Babashka, linters)
* **Babashka tasks** — simple build interface
* **Clerk notebooks** — interactive + static documentation
* **GitHub Actions** — CI + Pages publishing
* **GitHub Pages** — automatic notebook site

The goal is simple:

> clone → install tools → run one command → everything works

No local setup drift.
No “works on my machine”.

---

## 1. Install prerequisites

You only need **mise**.

[https://mise.jdx.dev/getting-started.html](https://mise.jdx.dev/getting-started.html)

Add `mise activate` to your shell config (e.g. `~/.zshrc`) so that pinned tool versions are available directly — no `mise exec --` prefix needed.

Verify:

```bash
mise --version
```

---

## 2. First run (bootstrapping the project)

From the project root:

```bash
mise install
bb build
```

This will:

* download JVM + Clojure toolchain
* install linters + babashka
* run formatting checks
* lint the code
* run tests
* export Clerk notebooks → `public/`

You now have a fully working environment.

---

## 3. Development workflow

A typical session has two things running side by side: a **REPL** (for evaluating code) and **Clerk** (for rendering notebooks in the browser). You write code in notebook files — they are normal `.clj` files that Clerk also knows how to render.

### Starting a session

1. **Start Clerk** — `bb clerk` in a terminal. Watches `notebooks/` and serves on [localhost:7777](http://localhost:7777). Keep running.
2. **Start REPL** — use IntelliJ/Cursive jack-in (with `:dev` alias). This connects the editor to a running Clojure process so you can evaluate code from any file.

### Writing code

3. **Open a notebook** — open a chapter file in `notebooks/` (e.g. `ch03_do_things_crash_course.clj`) in IntelliJ. This is a regular Clojure file. You write code here.
4. **Evaluate with the REPL** — use Cursive's shortcuts to send forms to the REPL (e.g. Ctrl+Enter for a single form). You get instant feedback in the editor.
5. **See rendered output in Clerk** — when you save the file, Clerk auto-reloads it in the browser. It shows the results of each form plus any `clerk/md` prose, formatted as a readable document.

The loop is: **write in the notebook, evaluate via REPL to test, save to see it rendered in Clerk**.

### Graduating code to src

6. **Promote to src** — once code stabilises, extract functions into `src/brave/`. The notebook then `require`s and calls those functions, becoming documentation rather than scratch code.
7. **Add tests** — write tests in `test/brave/` for promoted code.
8. **Quality check** — `bb check` (style + lint + splint + test) before committing.

### Chapter notebook convention

Notebook export auto-discovers files matching:

`notebooks/chNN_topic.clj`

Where `NN` is a two-digit chapter number (`01` … `13`).
No manual export list is needed.

This repo ships one notebook per chapter:

`ch01_building_running_and_repl.clj` through `ch13_multimethods_protocols_and_records.clj`.

It also includes `notebooks/index.clj` as a table-of-contents homepage that links to all chapter notebooks.

### Individual commands

```bash
bb clerk            # Start Clerk notebook server
bb test             # Run tests via kaocha
bb lint             # Lint with clj-kondo (src + test)
bb lint-all         # Lint all code (src + test + dev + notebooks)
bb fmt              # Auto-fix formatting (cljstyle)
bb check            # Full quality gate (style/lint all + splint + test)
bb build            # Full build (check + clerk export)
bb clean            # Remove build artifacts and caches
```

---

## 4. Project structure

```
src/        production code
dev/        development entrypoints (Clerk, tooling)
test/       tests
notebooks/  Clerk notebooks (source of documentation)
public/     generated static site (DO NOT EDIT)
```

---

## 5. Static documentation site

Every push to `main`:

1. CI runs quality checks (`bb check`)
2. Pages workflow exports chapter notebooks (`bb clerk-export`)
3. GitHub Pages publishes `public/`

So your notebooks become a living website.

---

## 6. Logging

SLF4J simple logger is enabled automatically.

Example:

```clojure
(import 'org.slf4j.LoggerFactory)

(def log (LoggerFactory/getLogger "demo"))

(.info log "Hello logs")
```

---

## 7. Philosophy of this repo

This project is intentionally opinionated:

* Toolchain is pinned (mise)
* Tasks are explicit (babashka)
* Docs are executable (Clerk)
* CI equals local build
* The notebook is the product

You should never need to ask:

> "how do I run this?"

---

## 8. One-command mental model

After cloning:

```bash
mise install
bb build
```

Everything else is optional.
