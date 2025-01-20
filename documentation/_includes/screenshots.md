{% if project["library"]["screenshots"] | length > 0 %}

## :camera: Screenshots 

{% for screenshot in project["library"]["screenshots"] %}
<table> 
    <thead>
        <tr><th>{{ screenshot["name"] }}</th><th></th></tr>
    </thead>
    <tbody>
        <tr>
        {% for image in screenshot["images"] %}
		    {% if (loop.index - 1) % 3 == 0 %}
			</tr><tr>
		    {% endif %}
            <td><img src="{{ image }}" alt="{{ image }}" height="400"></td>
        {% endfor %}
        </tr>
    </tbody>
</table>
{% endfor %}

{% endif %}
