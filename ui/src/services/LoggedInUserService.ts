import { UserRole } from "@/utils/Enums";

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
