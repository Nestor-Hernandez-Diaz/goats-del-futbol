# Carpeta de Imágenes

## Imágenes Disponibles

### Imagen de Fondo Principal
- **goats-trio-bg.jpg** (1024x1024 píxeles)
  - Descripción: Imagen de fondo principal con los tres GOATs del fútbol
  - Uso: Fondo de la sección hero en index.html
  - Alt text: Se usa como background-image en CSS

### Banners de Jugadores (Hero Images)
- **messi-hero-banner.png** (1024x1024 píxeles)
  - Descripción: Banner principal de Lionel Messi
  - Uso: Página individual de Messi (pages/messi.html)
  - Alt text: "Lionel Messi celebrando con la camiseta argentina - Banner principal"

- **ronaldo-hero-banner.png** (1024x1024 píxeles)
  - Descripción: Banner principal de Cristiano Ronaldo
  - Uso: Página individual de Ronaldo (pages/ronaldo.html)
  - Alt text: "Cristiano Ronaldo en pose icónica con camiseta de Portugal - Banner principal"

- **neymar-hero-banner.png** (1024x1024 píxeles)
  - Descripción: Banner principal de Neymar Jr
  - Uso: Página individual de Neymar (pages/neymar.html)
  - Alt text: "Neymar Jr sonriendo con la camiseta de Brasil - Banner principal"

### Imagen Placeholder
- **placeholder.svg** (SVG - formato vectorial)
  - Descripción: Imagen placeholder para contenido faltante
  - Uso: Imagen de respaldo cuando otras imágenes no están disponibles

## Notas Importantes

### Imágenes Faltantes
Las siguientes imágenes están referenciadas en el HTML pero no existen físicamente:
- Imágenes de perfil de jugadores (messi.jpg, ronaldo.jpg, neymar.jpg)
- Iconos de jugadores (messi-icon.jpg, ronaldo-icon.jpg, neymar-icon.jpg)
- Imágenes de clubes y selecciones
- Imágenes de galería y momentos históricos
- Miniaturas de videos

### Especificaciones Técnicas
- **Formato recomendado**: JPG para fotografías, PNG para imágenes con transparencia, SVG para iconos
- **Tamaño estándar**: 1024x1024 píxeles para banners principales
- **Optimización**: Las imágenes deben estar optimizadas para web
- **Alt text**: Cada imagen debe tener un atributo alt único y descriptivo

### Convenciones de Nomenclatura
- `{jugador}-hero-banner.png`: Banners principales de jugadores
- `{jugador}-profile.jpg`: Fotos de perfil
- `{jugador}-icon.jpg`: Iconos pequeños
- `{club/seleccion}-logo.png`: Logos de equipos
- `{descripcion}-{contexto}.jpg`: Imágenes específicas

Esta carpeta contiene todas las imágenes generales utilizadas en el sitio web GOATS Fútbol.

## Contenido Recomendado

### Banners (`banners/`)
- **Nombre**: `banner-[ubicacion]-[version].jpg`
- **Ejemplo**: `banner-home-principal.jpg`, `banner-messi-hero.jpg`
- **Tamaño**: 1920x600px (desktop), 800x400px (móvil)
- **Formato**: JPG
- **Peso máximo**: 800KB
- **Cantidad necesaria**: 3 banners principales para home, 1 banner por cada página de jugador

### Fondos (`backgrounds/`)
- **Nombre**: `bg-[seccion]-[variante].jpg`
- **Ejemplo**: `bg-estadio-oscuro.jpg`, `bg-pattern-futbol.png`
- **Tamaño**: 1920x1080px o patrón repetible de 400x400px
- **Formato**: JPG (fondos completos), PNG (con transparencia)
- **Peso máximo**: 500KB
- **Cantidad necesaria**: 1 fondo principal, 2-3 variantes, 1-2 patrones

### Logos (`logos/`)
- **Nombre**: `logo-goats-[variante].svg`
- **Ejemplo**: `logo-goats-color.svg`, `logo-goats-blanco.svg`
- **Tamaño**: 300x100px (aproximado, mantener proporción)
- **Formato**: SVG (preferido), PNG (alternativa con transparencia)
- **Peso máximo**: 100KB
- **Cantidad necesaria**: Logo principal, versión monocromática, versión para fondo oscuro

### Elementos UI (`ui/`)
- **Nombre**: `ui-[elemento]-[estado].png`
- **Ejemplo**: `ui-boton-hover.png`, `ui-separador.png`
- **Tamaño**: Variable según elemento
- **Formato**: PNG con transparencia, SVG para elementos vectoriales
- **Peso máximo**: 50KB
- **Cantidad necesaria**: Botones, separadores, decoradores, indicadores

## Formatos Recomendados

- **JPG**: Fotografías y banners (calidad 80-85%)
- **PNG**: Elementos con transparencia (8-bit para gráficos simples)
- **SVG**: Logos, iconos y elementos vectoriales
- **WebP**: Alternativa moderna para mejor compresión (proporcionar JPG como fallback)

## Optimización

- Todas las imágenes deben estar optimizadas para web
- Utilizar herramientas como TinyPNG, ImageOptim o Squoosh
- Incluir atributos width/height en HTML para evitar layout shifts
- Considerar versiones responsive con diferentes tamaños