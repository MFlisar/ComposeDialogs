{% if project["modules"] is not defined or project["modules"] | length == 1 %}

=== "Dependencies"

    Simply add the dependencies inside your `build.gradle.kts` file.

    ```kotlin title="build.gradle.kts"

    val {{ project["library"]["name"] | lower }} = "<LATEST-VERSION>"

    implementation("{{ project["library"]["maven"] }}:{{ project["library"]["maven-main-library"] }}:${{ project["library"]["name"] | lower }}")
    ```

=== "Version Catalog"

    Define the dependencies inside your `libs.versions.toml` file.

    ```toml title="libs.versions.toml"
    [versions]
    {{ project["library"]["name"] | lower }} = "<LATEST-VERSION>"
    
    [libraries]
   
    {%- set module = project["library"]["maven-main-library"] -%}
    {%- set name = project["library"]["name"] | lower ~ " =" -%}
    {% set module2 = "\"" ~ project["library"]["maven"] ~ ":" ~ module ~ "\"," %}
    {{ name }} { module = {{ module2 }} version.ref = "{{ project["library"]["name"] | lower }}" }
    ```

    And then use the definitions in your projects like following:

    ```kotlin title="build.gradle.kts"
    implementation(libs.{{ project["library"]["name"] | lower }})
    ```

{% endif %}