#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in float lineEdge;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor * ColorModulator;
    color.a *= exp(-4.0 * lineEdge * lineEdge);

    if (color.a < 0.001) discard;

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}