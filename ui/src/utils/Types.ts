export interface Principal {
  [key: string]: string;
  name: string;
  organization: string;
  email: string;
  phone: string;
}

export interface Organization {
  id: number;
  name: string;
}

export interface ManagerNode {
  id: number;
  tags: string[];
  clientDN: ClientDN;
  lastContact: string;
  apiKey: string;
  notes: { [key: string]: string };
}

interface ClientDN {
  CN: string;
  O: string;
  L: string;
}
