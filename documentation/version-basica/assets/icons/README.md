# Carpeta de Iconos

Esta carpeta contiene todos los iconos utilizados en el sitio web GOATS Fútbol.

## Contenido Recomendado

### Favicon (`favicon/`)
- **Nombre**: `favicon.svg` (principal), `favicon-[tamaño].png` (alternativas)
- **Ejemplo**: `favicon.svg`, `favicon-32x32.png`, `favicon-192x192.png`
- **Tamaños requeridos**: 
  - SVG (vector principal)
  - 16x16px, 32x32px, 48x48px (PNG)
  - 180x180px (apple-touch-icon)
  - 192x192px, 512x512px (PWA)
- **Formato**: SVG (principal), PNG (alternativas)
- **Peso máximo**: 10KB (SVG), 5KB (PNG pequeños), 20KB (PNG grandes)
- **Cantidad necesaria**: 1 SVG + 5 PNG en diferentes tamaños

### Redes Sociales (`social/`)
- **Nombre**: `icon-[red]-[variante].svg`
- **Ejemplo**: `icon-facebook.svg`, `icon-twitter-alt.svg`
- **Tamaño**: 24x24px (consistente para todos)
- **Formato**: SVG (preferido)
- **Peso máximo**: 5KB
- **Cantidad necesaria**: 5 iconos (Facebook, Twitter, Instagram, YouTube, TikTok)

### UI (`ui/`)
- **Nombre**: `icon-[accion].svg`
- **Ejemplo**: `icon-menu.svg`, `icon-search.svg`, `icon-arrow-right.svg`
- **Tamaño**: 24x24px (consistente)
- **Formato**: SVG
- **Peso máximo**: 3KB
- **Cantidad necesaria**: 10-15 iconos para navegación y acciones comunes

### Banderas (`flags/`)
- **Nombre**: `flag-[codigo-pais].svg`
- **Ejemplo**: `flag-ar.svg` (Argentina), `flag-pt.svg` (Portugal), `flag-br.svg` (Brasil)
- **Tamaño**: 32x24px (proporción 4:3)
- **Formato**: SVG
- **Peso máximo**: 5KB
- **Cantidad necesaria**: 5-10 banderas de países relevantes para los jugadores

## Formatos y Especificaciones Técnicas

- **SVG**: Formato principal para todos los iconos
  - Optimizar con SVGO o similar
  - Eliminar metadatos innecesarios
  - Usar viewBox adecuado
  - Simplificar paths cuando sea posible

- **PNG**: Solo como alternativa para favicons o cuando SVG no sea soportado
  - Usar compresión óptima
  - Mantener transparencia cuando sea necesario
  - Generar desde SVG para mantener consistencia

## Implementación

- Usar sprite SVG para iconos de UI cuando sea posible
- Implementar favicons mediante etiquetas link en el head
- Asegurar que todos los iconos tengan buen contraste en diferentes fondos