import { UserRole } from "@/utils/Enums";

// TODO add Keycloak integration and routing
class LoggedInUserService {
    static getRole(): UserRole {
        return UserRole.IT;
    }

    static getUsername(): string {
        return "John Doe";
    }

    static hasRole(role: UserRole): boolean {
        return this.getRole() === role;
    }
}

export default LoggedInUserService;
