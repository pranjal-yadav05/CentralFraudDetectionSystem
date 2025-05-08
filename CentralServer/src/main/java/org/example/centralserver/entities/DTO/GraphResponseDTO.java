package org.example.centralserver.entities.DTO;



import lombok.Data;

import java.util.List;

@Data
public class GraphResponseDTO {
    private List<NodeDTO> nodes;
    private List<LinkDTO> links;

    public GraphResponseDTO() {}

    public GraphResponseDTO(List<NodeDTO> nodes, List<LinkDTO> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public List<NodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<LinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDTO> links) {
        this.links = links;
    }
}