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

Verify:

```bash
mise --version
```

---

## 2. First run (bootstrapping the project)

From the project root:

```bash
mise install
mise exec -- bb build
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

### Start interactive notebooks

```bash
mise exec -- bb clerk
```

Open:

```
http://localhost:7777
```

The `notebooks/` folder auto-reloads.

---

### Run tests

```bash
mise exec -- bb test
```

---

### Lint code

```bash
mise exec -- bb lint
```

---

### Format code

```bash
mise exec -- bb fmt
```

---

### Full quality gate

```bash
mise exec -- bb check
```

---

### Full build (same as CI)

```bash
mise exec -- bb build
```

Runs:

```
format → lint → test → export notebooks
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

1. CI builds project
2. Clerk exports notebooks
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
mise exec -- bb build
```

Everything else is optional.

