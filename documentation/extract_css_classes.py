#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script para extraer todas las clases CSS del proyecto
"""

import re
import os

def extract_css_classes(css_file_path):
    """Extrae todas las clases CSS de un archivo"""
    classes = set()
    
    try:
        with open(css_file_path, 'r', encoding='utf-8') as file:
            content = file.read()
            
        # Buscar todas las clases CSS (que empiecen con .)
        class_pattern = r'\.([a-zA-Z][a-zA-Z0-9_-]*)'
        matches = re.findall(class_pattern, content)
        
        for match in matches:
            classes.add(match)
            
    except Exception as e:
        print(f"Error leyendo archivo CSS: {e}")
        
    return sorted(list(classes))

def extract_html_classes(html_file_path):
    """Extrae todas las clases usadas en un archivo HTML"""
    classes = set()
    
    try:
        with open(html_file_path, 'r', encoding='utf-8') as file:
            content = file.read()
            
        # Buscar atributos class en HTML
        class_pattern = r'class=["\']([^"\']*)["\']'
        matches = re.findall(class_pattern, content)
        
        for match in matches:
            # Dividir por espacios para obtener clases individuales
            individual_classes = match.split()
            for cls in individual_classes:
                if cls.strip():
                    classes.add(cls.strip())
                    
    except Exception as e:
        print(f"Error leyendo archivo HTML {html_file_path}: {e}")
        
    return sorted(list(classes))

def main():
    # Rutas de archivos
    css_file = "css/styles.css"
    html_files = [
        "index.html",
        "pages/messi.html", 
        "pages/neymar.html",
        "pages/ronaldo.html"
    ]
    
    print("=== EXTRAYENDO CLASES CSS ===")
    css_classes = extract_css_classes(css_file)
    print(f"Clases encontradas en CSS ({len(css_classes)}):")
    for cls in css_classes:
        print(f"  .{cls}")
    
    print("\n=== EXTRAYENDO CLASES DE HTML ===")
    all_html_classes = set()
    
    for html_file in html_files:
        if os.path.exists(html_file):
            print(f"\nClases en {html_file}:")
            html_classes = extract_html_classes(html_file)
            for cls in html_classes:
                print(f"  {cls}")
                all_html_classes.add(cls)
        else:
            print(f"Archivo no encontrado: {html_file}")
    
    print(f"\n=== RESUMEN ===")
    print(f"Total clases en CSS: {len(css_classes)}")
    print(f"Total clases únicas en HTML: {len(all_html_classes)}")
    
    # Verificar clases que están en HTML pero no en CSS
    missing_in_css = all_html_classes - set(css_classes)
    if missing_in_css:
        print(f"\nClases en HTML que NO están en CSS ({len(missing_in_css)}):")
        for cls in sorted(missing_in_css):
            print(f"  {cls}")
    
    # Verificar clases que están en CSS pero no se usan en HTML
    unused_in_html = set(css_classes) - all_html_classes
    if unused_in_html:
        print(f"\nClases en CSS que NO se usan en HTML ({len(unused_in_html)}):")
        for cls in sorted(unused_in_html):
            print(f"  {cls}")

if __name__ == "__main__":
    main()