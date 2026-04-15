"use client";
import { Wallet, ChevronDown, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { nav, methodColor } from "@/lib/nav";

export function Sidebar({
  active,
  expanded,
  token,
  onSelect,
  onToggle,
  onLogout,
}: {
  active: string;
  expanded: Record<string, boolean>;
  token: string;
  onSelect: (id: string) => void;
  onToggle: (id: string) => void;
  onLogout: () => void;
}) {
  return (
    <aside className="w-72 border-r border-border bg-card flex flex-col">
      <div className="p-4 border-b border-border">
        <div className="flex items-center gap-2">
          <Wallet className="h-5 w-5 text-primary" />
          <h1 className="font-bold">Banking API</h1>
        </div>
        <p className="text-xs text-muted-foreground mt-1">
          Microservices Tester
        </p>
      </div>
      <nav className="flex-1 overflow-auto py-2">
        {nav.map((group) => (
          <div key={group.id}>
            <button
              onClick={() => onToggle(group.id)}
              className="w-full flex items-center gap-2 px-4 py-2 text-sm font-medium text-muted-foreground hover:text-foreground hover:bg-accent/50 transition-colors"
            >
              {expanded[group.id] ? (
                <ChevronDown className="h-3.5 w-3.5" />
              ) : (
                <ChevronRight className="h-3.5 w-3.5" />
              )}
              <group.icon className="h-4 w-4" />
              <span className="flex-1 text-left">{group.label}</span>
              <Badge variant="outline" className="text-[10px] px-1.5 font-mono">
                {group.port}
              </Badge>
            </button>
            {expanded[group.id] && (
              <div className="ml-4 border-l border-border">
                {group.items.map((item) => (
                  <button
                    key={item.id}
                    onClick={() => onSelect(item.id)}
                    className={`w-full flex items-center gap-2 pl-4 pr-3 py-1.5 text-xs transition-colors ${active === item.id ? "bg-primary/10 text-primary" : "text-muted-foreground hover:text-foreground hover:bg-accent/30"}`}
                  >
                    <span
                      className={`font-mono font-bold text-[10px] w-11 text-left ${methodColor[item.method]}`}
                    >
                      {item.method}
                    </span>
                    <span className="truncate">{item.label}</span>
                  </button>
                ))}
              </div>
            )}
          </div>
        ))}
      </nav>
      <div className="p-3 border-t border-border">
        {token ? (
          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <div className="h-2 w-2 rounded-full bg-green-500 animate-pulse" />
              <span className="text-xs text-green-400">Authenticated</span>
            </div>
            <p className="text-[10px] text-muted-foreground font-mono truncate">
              {token.substring(0, 36)}...
            </p>
            <Button
              variant="outline"
              size="sm"
              className="w-full text-xs h-7"
              onClick={onLogout}
            >
              Logout
            </Button>
          </div>
        ) : (
          <div className="flex items-center gap-2">
            <div className="h-2 w-2 rounded-full bg-red-500" />
            <span className="text-xs text-red-400">Not authenticated</span>
          </div>
        )}
      </div>
    </aside>
  );
}
