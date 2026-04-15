"use client";
import { useState } from "react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ScrollArea } from "@/components/ui/scroll-area";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

const methodColor: Record<string, string> = {
  GET: "bg-green-600",
  POST: "bg-yellow-600",
  PUT: "bg-blue-600",
  PATCH: "bg-orange-600",
  DELETE: "bg-red-600",
};

export function ConfirmDialog({
  method,
  url,
  body,
  trigger,
  onConfirm,
}: {
  method: string;
  url: string;
  body?: unknown;
  trigger: React.ReactNode;
  onConfirm: () => void;
}) {
  const [open, setOpen] = useState(false);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <div onClick={() => setOpen(true)}>{trigger}</div>
      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            Confirm Request
          </DialogTitle>
          <DialogDescription>
            Kiểm tra thông tin request trước khi gửi.
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-3">
          <div className="flex items-center gap-2">
            <Badge
              className={`${methodColor[method]} text-white font-mono text-xs`}
            >
              {method}
            </Badge>
            <code className="text-sm text-muted-foreground break-all">
              {url}
            </code>
          </div>

          {body !== undefined && body !== null && (
            <div>
              <p className="text-xs font-medium text-muted-foreground mb-1">
                Request Body
              </p>
              <ScrollArea className="max-h-48">
                <pre className="text-xs font-mono bg-muted/50 rounded-lg p-3 whitespace-pre-wrap">
                  {typeof body === "string"
                    ? body
                    : JSON.stringify(body, null, 2)}
                </pre>
              </ScrollArea>
            </div>
          )}
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={() => setOpen(false)}>
            Cancel
          </Button>
          <Button
            onClick={() => {
              setOpen(false);
              onConfirm();
            }}
          >
            Send Request
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
