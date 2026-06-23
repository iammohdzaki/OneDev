# Kotlin Theme Engine

The Kotlin Theme Engine is a dynamic runtime theming library for **Compose Multiplatform**. It allows applications to switch and swap Material 3 color schemes dynamically without having to rebuild the entire Composable tree.

## Advantages
- **Instant Tonal Changes**: Fast color swaps with smooth color transitions.
- **CompositionLocal Providers**: Exposes a reactive state so any nested element can access and update the current colors.
- **Tonal Palette Generation**: Auto-generates matching primary, secondary, container, and outline variants from a single primary key color.
