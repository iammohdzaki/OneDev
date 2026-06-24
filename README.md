# OneDev | Kotlin Multiplatform Developer Portfolio

A premium, modern developer portfolio built entirely with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform** targeting Web/WasmJs. Employs a terminal-inspired, neo-cyberpunk aesthetic using the GitHub Dark color palette.

This portfolio is **100% data-driven**—all personal details, social accounts, professional experiences, and projects are managed dynamically using JSON configurations. No hardcoded Kotlin code modifications are required to make it your own!

---

## 🚀 Key Features

- **⚡ Blazing Fast WasmJs Target**: Compiled directly to WebAssembly (WasmJs) for optimal web performance.
- **🎨 GitHub Dark Theme**: High-fidelity terminal look with smooth gradients and active hover transitions.
- **✍️ Interactive Typewriter Hero**: Dynamic animated tagline with a blinking cursor.
- **⚙️ Fully Configurable**: Customized entirely via `profile.json` and `projects.json`.
- **🛠️ Markdown Projects**: Supports rich markdown rendering for project description sub-pages.
- **📱 Responsive Layout**: Adapts smoothly to narrow mobile screens and wide desktop displays.
- **📦 Optimized CI/CD**: Automatic and manual GitHub Pages deployment workflow with pre-configured **Gzip compression** to reduce loading times.

---

## 🛠️ How to Customize

All config files are located under:
📂 `shared/src/commonMain/composeResources/files/`

### 1. Personalize Your Profile (`profile.json`)
You can edit the personal bio, tagline, stats, work experience, skill levels, and core philosophies.

> [!NOTE]  
> The top-level `github`, `linkedin`, and `email` fields have been cleaned up to prevent redundancy. They are dynamically parsed from the `socials` array automatically!

**Key Configuration Fields:**
- `name`: Your full name (e.g., `"Mohammad Zaki"`).
- `tagline`: The tagline printed with a typewriter effect in the Hero section.
- `bio`: A short professional bio displayed under the typewriter.
- `cvUrl`: Path to your curriculum vitae. Set it to `"resume.pdf"` to open `resume.pdf` in a new tab.
- `discordUrl`: Optional. Link to your Discord profile/server. Displays a themed Discord action button.
- `socials`: A list of social link objects containing `label` and `url`. Supported labels: `Github`, `LinkedIn`, `Email`.
- `skillBars`: A list of skills showing a name and proficiency percentage.
- `experiences`: Professional experience timeline cards with highlights.
- `philosophy`: Core developer values and values emojis.

---

### 2. Configure Your Projects (`projects.json`)
Manage all featured and standalone projects.

**Project Object Structure:**
```json
{
  "id": "my-project",
  "title": "My Awesome Library",
  "description": "A description of what the project does.",
  "category": "Library",
  "tags": ["Kotlin", "KMP", "Android"],
  "stars": 42,
  "repoUrl": "https://github.com/yourusername/my-project",
  "projectUrl": "",
  "group": "Featured",
  "groupEmoji": "⭐",
  "isPinned": true,
  "details": "projects/my-project.md",
  "currentWork": "Adding unit tests"
}
```

---

### 3. Add Project Details Markdown Files
If you specify a `details` path in `projects.json` (e.g., `"details": "projects/my-project.md"`), create a markdown file under:
📂 `shared/src/commonMain/composeResources/files/projects/my-project.md`

#### Recommended Project Markdown Template:
```markdown
# My Awesome Library

A Kotlin Multiplatform library that solves app architecture issues.

## Features
- 🚀 Multiplatform support (Android, iOS, Web)
- ⚡ 100% thread-safe coroutines
- 📦 Zero external dependencies

## Installation
```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

## Quick Start
```kotlin
val sdk = MyLibrary.initialize()
sdk.start()
```
```

---

## 📂 Hosting Your Resume File
To add your resume/CV:
1. Place your PDF file under:
   📂 `webApp/src/webMain/resources/resume.pdf`
2. Set `"cvUrl": "resume.pdf"` in `profile.json`.
   
This configuration ensures the resume is served directly from the root of your site (e.g., `yoursite.com/resume.pdf`) so clicking the **view_cv.sh** button opens it directly in a new tab without forcing a download.

---

## 🏃 Local Execution

Ensure you have Java 17 or higher installed. Use the following commands to run the application locally:

### 1. Run Wasm Target (Recommended)
Fast incremental compiles, optimized for modern browsers.
```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

### 2. Run JS Target (Legacy/Fallback)
```bash
./gradlew :webApp:jsBrowserDevelopmentRun
```

The browser will open the dev server at `http://localhost:8080/`.

---

## 🚀 GitHub Pages Deployment (with Gzip)

A pre-configured CI/CD workflow is included in `.github/workflows/deploy.yml` that handles automatic deployment when you push to `main`, and supports manual builds.

### Setting Up GitHub Pages:
1. Push your code to your GitHub repository.
2. Go to repository **Settings** -> **Pages**.
3. Under **Build and deployment**, select **Deploy from a branch** and choose the `gh-pages` branch.

### Manual Workflow Trigger:
1. Go to the **Actions** tab in your repository.
2. Select **Deploy Portfolio to GitHub Pages** in the left sidebar.
3. Click the **Run workflow** dropdown and click the green **Run workflow** button.

### ⚡ Gzip Loading Optimizations:
Kotlin WebAssembly files (`webApp.wasm`) can be large. The deployment workflow automatically pre-compresses `webApp.wasm`, `webApp.js`, and `index.html` using Gzip max-level compression (`gzip -9`) during the build step.
For web servers that support pre-compressed files, this cuts down initial page loads by up to **80%**!

---

## 🐛 Bug Reports & Issue Template

If you encounter any issues or want to request a feature, please open an issue in the repository.

### Recommended Issue Template:
```markdown
**Describe the bug**
A clear and concise description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected behavior**
A clear and concise description of what you expected to happen.

**Desktop/Browser (please complete the following information):**
- OS: [e.g. Windows, macOS]
- Browser [e.g. Chrome, Firefox, Safari]
- Version [e.g. 124.0.0]
```

---

## 📄 License

This project is open-source and licensed under the **MIT License**. See the `LICENSE` file for more details.