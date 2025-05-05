[![Maven](https://img.shields.io/maven-central/v/{{ project["library"]["maven"] }}/{{ project["library"]["maven-main-library"] }}?style=for-the-badge&color=blue)](https://central.sonatype.com/namespace/{{ project["library"]["maven"] }}){:target="_blank"}
![API](https://img.shields.io/badge/api-{{ project["library"]["api"] }}%2B-brightgreen.svg?style=for-the-badge)
![Kotlin](https://img.shields.io/github/languages/top/{{ project["library"]["github"] }}.svg?style=for-the-badge&color=blueviolet)
{% if project["library"]["multiplatform"] -%}
![KMP](https://img.shields.io/badge/Kotlin_Multiplatform-blue?style=for-the-badge&label=Kotlin)
{%- endif -%}
[![License](https://img.shields.io/github/license/{{ project["library"]["github"] }}?style=for-the-badge)](https://github.com/{{ project["library"]["github"] }}/blob/{{ project["library"]["branch"] }}/LICENSE){:target="_blank"}

<h1 align="center"><b>{{ project["library"]["name"] }}</b></h1>

![PLATFORMS](https://img.shields.io/badge/PLATFORMS-black?style=for-the-badge){:class=exclude-glightbox }
{% if "jvm" in project["library"]["platforms"] -%}
![JVM](https://img.shields.io/badge/JVM-grey?style=for-the-badge){:class=exclude-glightbox }
{% endif -%}
{% if "android" in project["library"]["platforms"] -%}
![ANDROID](https://img.shields.io/badge/Android-green?style=for-the-badge){:class=exclude-glightbox }
{% endif -%}
{% if "ios" in project["library"]["platforms"] -%}
![IOS](https://img.shields.io/badge/iOS-blue?style=for-the-badge){:class=exclude-glightbox }
{% endif -%}
{% if "js" in project["library"]["platforms"] -%}
![JS](https://img.shields.io/badge/js-orange?style=for-the-badge){:class=exclude-glightbox }
{% endif -%}
{% if "wasm" in project["library"]["platforms"] -%}
![WASM](https://img.shields.io/badge/wasm-purple?style=for-the-badge){:class=exclude-glightbox }
{% endif -%}
{% if "linux" in project["library"]["platforms"] -%}
![LINUX](https://img.shields.io/badge/linux-yellow?style=for-the-badge){:class=exclude-glightbox }
{% endif -%}