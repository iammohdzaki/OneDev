# FixLag Gradle Plugin

The FixLag Gradle Plugin automates the setup of the FixLag diagnostics tool. It intercepts the compilation process using ASM class manipulation and injects tracking hooks directly into your class files with zero developer configuration required.

## Benefits
- **Zero-config Setup**: Simply apply the plugin, and classes are instrumented automatically.
- **Low Overhead**: Class manipulation is completed at build time, meaning no runtime compilation delay.
- **Trace Injections**: Safely traces constructor, lifecycle, and Compose state changes.
