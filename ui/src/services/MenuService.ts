import { UserRole, RequestState, ExecutionState } from "@/utils/Enums";

export class MenuService {
    static getUserMenu(localize: (key: string) => string): MenuItem[] {
        return [
            {
                label: "<username>",
                items: [
                    {
                        label: `${localize("navigation.settings")}`,
                        icon: "pi pi-cog",
                    },
                    {
                        label: `${localize("navigation.logout")}`,
                        icon: "pi pi-sign-out",
                    },
                ],
            },
        ];
    }

    static getRequestViewMenu(
        localize: (key: string) => string,
        userRole: UserRole,
        requestState: RequestState
    ): MenuItem[] {
        let menuItems: MenuItem[] = [];
        switch (requestState) {
            case RequestState.DRAFT:
                menuItems = [
                    { label: `${localize("menu.draft.publish")}` },
                    { label: `${localize("menu.draft.duplicate")}` },
                    { label: `${localize("menu.draft.delete")}` },
                    { label: `${localize("menu.draft.edit")}` },
                ];
                break;
            case RequestState.ONLINE:
                if (userRole === UserRole.IT) {
                    menuItems = [
                        { label: `${localize("menu.request.results")}` },
                        {
                            label: `${localize(
                                "menu.request.duplicateAsDraft"
                            )}`,
                        },
                        { label: `${localize("menu.request.close")}` },
                        { label: `${localize("menu.request.archive")}` },
                        { label: `${localize("menu.request.edit")}` },
                    ];
                }
                if (userRole === UserRole.DAC) {
                    menuItems = [
                        { label: `${localize("menu.request.results")}` },
                        { label: `${localize("menu.request.close")}` },
                        { label: `${localize("menu.request.archive")}` },
                    ];
                }
                break;
            case RequestState.CLOSED:
                if (userRole === UserRole.IT) {
                    menuItems = [
                        { label: `${localize("menu.request.results")}` },
                        {
                            label: `${localize(
                                "menu.request.duplicateAsDraft"
                            )}`,
                        },
                        { label: `${localize("menu.request.archive")}` },
                        { label: `${localize("menu.request.edit")}` },
                    ];
                }
                if (userRole === UserRole.DAC) {
                    menuItems = [
                        { label: `${localize("menu.request.results")}` },
                        { label: `${localize("menu.request.archive")}` },
                    ];
                }
                break;
            case RequestState.ARCHIVED:
                if (userRole === UserRole.IT) {
                    menuItems = [
                        { label: `${localize("menu.request.results")}` },
                        {
                            label: `${localize(
                                "menu.request.duplicateAsDraft"
                            )}`,
                        },
                    ];
                }
                if (userRole === UserRole.DAC) {
                    menuItems = [
                        { label: `${localize("menu.request.results")}` },
                    ];
                }
                break;
        }
        return menuItems;
    }

    static getRequestEditMenu(
        localize: (key: string) => string,
        requestState: RequestState
    ): MenuItem[] {
        if (requestState === RequestState.DRAFT) {
            return [
                { label: `${localize("menu.draft.save")}` },
                { label: `${localize("cancel")}` },
            ];
        } else {
            return [
                { label: `${localize("menu.request.save")}` },
                { label: `${localize("cancel")}` },
            ];
        }
    }

    static getExecutionMenu(
        localize: (key: string) => string,
        executionState: ExecutionState
    ): MenuItem[] {
        let menuItems: MenuItem[] = [];
        switch (executionState) {
            case ExecutionState.PENDING:
                menuItems = [{ label: `${localize("publishExecution")}` }];
                break;
            case ExecutionState.PUBLISHED:
                menuItems = [
                    { label: `${localize("closeExecution")}` },
                    { label: `${localize("archiveExecution")}` },
                    { label: `${localize("goToResults")}` },
                ];
                break;
            case ExecutionState.CLOSED:
                menuItems = [
                    { label: `${localize("archiveExecution")}` },
                    { label: `${localize("goToResults")}` },
                ];
                break;
            case ExecutionState.ARCHIVED:
                menuItems = [{ label: `${localize("goToResults")}` }];
                break;
        }
        return menuItems;
    }

    static getNodeViewMenu(localize: (key: string) => string): MenuItem[] {
        return [
            { label: `${localize("menu.node.unsubscribe")}` },
            { label: `${localize("menu.node.edit")}` },
        ];
    }

    static getNodeEditMenu(localize: (key: string) => string): MenuItem[] {
        return [
            { label: `${localize("menu.node.save")}` },
            { label: `${localize("cancel")}` },
        ];
    }
}

export interface MenuItem {
    label: string;
    icon?: string;
    command?: () => void;
    items?: MenuItem[];
}

export default MenuService;
