<script lang="ts">
import { ManagerNode } from "@/utils/Types";
import { TestDataService } from "@/services/TestDataService";

export default {
    props: {
        targetNodeIds: {
            type: Array as () => number[],
            required: true,
        },
        fieldSetHeight: {
            type: String as () => string,
            default: "h-48-4rem",
        },
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
        };
    },
    computed: {
        selectedNodes(): ManagerNode[] {
            return this.sortNodesById(
                this.allManagerNodes.filter((node) =>
                    this.targetNodeIds.includes(node.id)
                )
            );
        },
        availableNodes(): ManagerNode[] {
            return this.sortNodesById(
                this.allManagerNodes.filter(
                    (node) => !this.targetNodeIds.includes(node.id)
                )
            );
        },
    },
    methods: {
        async fetchManagerNodes(): Promise<void> {
            this.allManagerNodes = await TestDataService.getNodes();
        },
        sortNodesById(nodes: ManagerNode[]): ManagerNode[] {
            return nodes.sort((a, b) => a.id - b.id);
        },
        // TODO add filter by german date
        filterNodesBySearchTerm(
            nodes: ManagerNode[],
            searchTerm: string
        ): ManagerNode[] {
            return nodes.filter(
                (node) =>
                    node.id.toString().includes(searchTerm) ||
                    node.tags.some((tag) =>
                        tag.toLowerCase().includes(searchTerm)
                    ) ||
                    node.clientDN.CN.toLowerCase().includes(searchTerm)
            );
        },
    },
};
</script>
