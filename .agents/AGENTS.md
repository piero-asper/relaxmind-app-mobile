# Pautas de Desarrollo y Reglas de RelaxMind

## 1. Pantallas de Carga y Overlays (Evitar overlapping y desalineación)
- **NO usar** `LoadingIndicator()` sin un contenedor de posicionamiento adecuado. Por defecto es un `CircularProgressIndicator` simple y se alineará a la esquina superior izquierda si no se envuelve correctamente.
- **FullScreenLoadingOverlay**: Para bloquear interacciones en la pantalla actual con un fondo negro translúcido al 25% y un indicador de progreso centrado, usa `FullScreenLoadingOverlay()`.
- **FullScreenLoadingScreen**: Para procesos de guardado o transiciones de carga limpios con pantalla completa (como al guardar avatar o registrar notificaciones), usa `FullScreenLoadingScreen(text = "...")` y realiza un retorno anticipado (`return`) en el composable principal para que no se dibuje el `Scaffold` o el resto de componentes de fondo.
