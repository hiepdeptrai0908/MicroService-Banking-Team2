"use client";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Badge } from "@/components/ui/badge";
import type { ApiRes } from "@/lib/api";

export function ResultPanel({ res, label }: { res: ApiRes; label?: string }) {
  if (!res) return null;
  const ok = res.status >= 200 && res.status < 400;
  return (
    <div className="mt-3">
      {label && <p className="text-xs text-muted-foreground mb-1">{label}</p>}
      <div
        className={`rounded-lg border p-3 ${ok ? "border-green-800 bg-green-950/30" : "border-red-800 bg-red-950/30"}`}
      >
        <Badge variant={ok ? "default" : "destructive"} className="mb-2">
          {res.status || "Error"}
        </Badge>
        <ScrollArea className="max-h-48">
          <pre
            className={`text-xs font-mono whitespace-pre-wrap ${ok ? "text-green-300" : "text-red-300"}`}
          >
            {typeof res.data === "string"
              ? res.data
              : JSON.stringify(res.data, null, 2)}
          </pre>
        </ScrollArea>
      </div>
    </div>
  );
}
