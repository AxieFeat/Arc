#version 410

in vec4 vertexColor;
out vec4 FragColor;

void main()
{
    if(vertexColor.a <= 0) {
        discard;
    }
    FragColor = vertexColor;
}