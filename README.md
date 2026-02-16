# brave-clojure

A reproducible Clojure development workflow for working through [*Clojure for the Brave and True*](https://www.braveclojure.com/clojure-for-the-brave-and-true/).

* **mise** — pinned toolchain (Java, Clojure, Babashka, linters)
* **Babashka tasks** — simple build interface
* **Clerk notebooks** — interactive + static documentation
* **GitHub Actions** — CI + Pages publishing
* **GitHub Pages** — automatic notebook site

The goal is simple:

> clone, install tools, run one command, everything works

No local setup drift. No "works on my machine".

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
* export Clerk notebooks to `public/`

You now have a fully working environment.

---

## 3. The learning workflow

This section explains how you actually use this project day-to-day as you read through the book.

### Setup: Start your session

Before you begin a chapter, get two things running:

1. **Start Clerk** in a terminal — keep it running for your entire session:

```bash
bb clerk
```

This opens a browser at [localhost:7777](http://localhost:7777). Clerk watches the `notebooks/` directory.

2. **Connect a REPL** from your editor using jack-in (IntelliJ/Cursive, Emacs/CIDER, VS Code/Calva). Alternatively, start a standalone REPL with `bb dev-repl`.

You now have two complementary feedback loops:
- **REPL** — instant feedback on individual expressions (no save needed)
- **Clerk** — rendered document view of the whole notebook (on save)

### Step 1: Read the book chapter

Open the relevant chapter of [*Clojure for the Brave and True*](https://www.braveclojure.com/clojure-for-the-brave-and-true/) in your browser or book. Read through the concepts and examples.

### Step 2: Write code in the notebook

Each chapter has a corresponding notebook file:

```
notebooks/ch03_do_things_crash_course.clj
notebooks/ch04_core_functions_in_depth.clj
...
```

Open the chapter's notebook in your editor. Write code **directly in the notebook file** — this is a normal `.clj` file that Clerk knows how to render as a document with prose and evaluated results.

**This is where all your experimentation happens.** Type the examples from the book, tweak them, break them, try your own variations. The notebook is your scratchpad and your notes combined.

### Step 3: Get feedback with REPL and Clerk

As you write code in the notebook, use both feedback loops:

- **Evaluate inline with the REPL** — place your cursor on an expression and eval it (the keybinding depends on your editor). You get the result instantly, without saving. This is your primary tool for trying things out.
- **Save the file for Clerk** — when you want to see the whole notebook rendered with all results, prose (`clerk/md`), and formatting, save the file. Clerk re-evaluates everything and updates the browser.

**The REPL and Clerk are complementary, not alternatives.** The REPL is for rapid per-expression feedback while you write. Clerk is for seeing the bigger picture — your notebook as a living document. You don't write in the REPL and copy to the notebook; you write once in the notebook and use the REPL to evaluate expressions in place.

### Step 4: Back to the book

Read the next section, write more code, evaluate, save, repeat. The cycle is:

```
Read → Write in notebook → Eval with REPL → Save for Clerk → Read more
```

### Step 5: Graduate stable code to `src/`

As you work through chapters, some code will become reusable — a helper function, a data transformation, something you want to call from multiple notebooks or test properly. When that happens:

1. **Create a namespace in `src/brave/`** — for example `src/brave/exercises.clj` with namespace `brave.exercises`
2. **Move the stable functions there** — cut them from the notebook, paste into the src file
3. **Require from the notebook** — add `[brave.exercises :as ex]` to the notebook's `:require` and call the functions
4. **Write tests** — add a corresponding test file `test/brave/exercises_test.clj`
5. **Run quality checks** — `bb check` (formatting, linting, style, tests)

**The key idea:** notebooks are for exploration and documentation. `src/` is for code that has graduated from exploration to something you trust and want to keep. Not everything needs to move — simple one-off examples can stay in the notebook forever.

---

## The workflow at a glance

```
               Start session
              /             \
        bb clerk        REPL jack-in
     (localhost:7777)   (editor-connected)
              \             /
               v           v
           Read book chapter
                  |
                  v
       Write code in notebooks/chNN_*.clj
              /             \
    Eval inline (REPL)   Save file (Clerk)
    instant per-expr     rendered document
    feedback             in browser
              \             /
               v           v
           Read next section...
                  |
                  v
            Code stabilises?
             |           |
             No          Yes
             |           |
             v           v
           Done    Move to src/brave/*.clj
                        |
                        v
                   Add test/brave/*_test.clj
                        |
                        v
                   bb check
                        |
                        v
                   git commit
```

---

## 4. Project structure

```
notebooks/  Clerk notebooks — one per chapter, this is where you work
src/        Production code — stable functions extracted from notebooks
test/       Tests for code in src/
dev/        Development helpers (Clerk server, REPL utilities)
public/     Generated static site (DO NOT EDIT — built by Clerk export)
```

### Chapter notebook convention

Notebook export auto-discovers files matching `notebooks/chNN_topic.clj` where `NN` is a two-digit chapter number (`01` to `13`). No manual export list is needed.

`notebooks/index.clj` is the table-of-contents homepage that links to all chapter notebooks.

---

## 5. Commands reference

```bash
# Start your session (both recommended)
bb clerk            # Start Clerk notebook server (localhost:7777)
bb dev-repl         # Start Clojure REPL with dev deps (or use editor jack-in)

# Quality checks (run before committing)
bb check            # Full gate: style + lint + splint + test
bb fmt              # Auto-fix formatting (cljstyle)
bb style-check      # Check formatting without fixing (src + test)
bb lint             # Lint with clj-kondo (src + test)
bb lint-all         # Lint all code (src + test + dev + notebooks)
bb splint           # Check idiomatic style with Splint
bb test             # Run tests via kaocha

# Run a single test
bb test --focus brave.notebooks-test/export-config-test

# Build and clean
bb build            # Full build (check + clerk export)
bb clean            # Remove build artifacts and caches
```

---

## 6. Static documentation site

Every push to `main`:

1. CI runs quality checks (`bb check`)
2. Pages workflow exports chapter notebooks (`bb clerk-export`)
3. GitHub Pages publishes `public/`

So your notebooks become a living website — your personal annotated version of the book.

---

## 7. Logging

SLF4J simple logger is enabled automatically.

Example:

```clojure
(import 'org.slf4j.LoggerFactory)

(def log (LoggerFactory/getLogger "demo"))

(.info log "Hello logs")
```

---

## 8. Philosophy

* Toolchain is pinned (mise) — everyone gets the same versions
* Tasks are explicit (babashka) — no hidden scripts or aliases
* Docs are executable (Clerk) — if the notebook renders, the code works
* CI equals local build — `bb check` runs the same checks locally and in CI
* The notebook is the product — everything else supports it
