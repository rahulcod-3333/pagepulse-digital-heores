import { useMemo, useState } from "react";
import api from "./services/api";

const DEFAULT_STATUS = "Enter a URL and hit scan to check its vitals.";

function App() {
  const [url, setUrl] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const health = useMemo(() => getHealth(result), [result]);
  const monitorState = loading ? "scanning" : error ? "critical" : health.state;
  const monitorLabel = loading ? "SCANNING" : error ? "ERROR" : health.label;
  const statusState = loading ? "" : error ? "critical" : health.state;
  const statusText = loading
    ? "Fetching and analyzing the page..."
    : error
      ? "Scan failed - see details above."
      : health.statusText;

  async function runScan(event) {
    event.preventDefault();

    const trimmedUrl = url.trim();
    if (!trimmedUrl) {
      setError({
        title: "MISSING URL",
        message: "Enter a URL before scanning.",
      });
      setResult(null);
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    try {
    const response = await api.post("/audit", { url: trimmedUrl }, { timeout: 15000 });
    setResult(response.data);
  } catch (scanError) {
    if (scanError.response) {
      const status = scanError.response.status;
      setError({
        title: getErrorTitle(status),
        message: scanError.response.data?.message || `Request failed with status ${status}.`,
      });
    } else if (scanError.code === "ECONNABORTED") {
      setError({
        title: "REQUEST TIMED OUT",
        message: "The scan took too long to respond. The site may be slow or unreachable — try again.",
      });
    } else {
      setError({
        title: "CONNECTION FAILED",
        message: "Could not reach the Page Pulse backend. Check that it's running and reachable.",
      });
    }
  } finally {
    setLoading(false);
  }
}
  return (
    <div className="wrap">
      <header>
        <div className="logo">
          PAGE <span>PULSE</span>
        </div>
        <div className="tagline"> WEBSITE VITALS MONITOR</div>
      </header>

      <form className="scanner" onSubmit={runScan}>
        <input
          aria-label="URL to audit"
          autoComplete="off"
          onChange={(event) => setUrl(event.target.value)}
          placeholder="https://example.com"
          spellCheck="false"
          type="text"
          value={url}
        />
        <button className="scan-btn" disabled={loading} type="submit">
          {loading ? "SCANNING..." : "SCAN ->"}
        </button>
      </form>

      <PulseMonitor label={monitorLabel} state={monitorState} />

      <div className={`status-line ${statusState}`}>
        <span className="status-dot" />
        <span>{statusText || DEFAULT_STATUS}</span>
      </div>

      {error && (
        <div className="error-panel show">
          <div className="err-title">{error.title}</div>
          <div className="err-msg">{error.message}</div>
        </div>
      )}

      {result ? <VitalsGrid result={result} /> : <EmptyState loading={loading} />}

      <footer>
        Built for{" "}
        <a
          href="https://digitalheroesco.com"
          rel="noopener noreferrer"
          target="_blank"
        >
          Digital Heroes Training Task
        </a>
      </footer>
    </div>
  );
}

function PulseMonitor({ label, state }) {
  return (
    <div className={`monitor ${state}`}>
      <div className="monitor-label">{label}</div>
      <div className="pulse-track" aria-hidden="true">
        <PulseSvg />
        <PulseSvg />
      </div>
    </div>
  );
}

function PulseSvg() {
  return (
    <svg viewBox="0 0 400 92" preserveAspectRatio="none">
      <path
        className="pulse-path"
        d="M0,46 L60,46 L75,40 L90,74 L105,12 L120,50 L135,46 L400,46"
      />
    </svg>
  );
}

function VitalsGrid({ result }) {
  const metrics = getMetrics(result);

  return (
    <div className="vitals">
      {metrics.map((metric, index) => (
        <VitalTile key={metric.label} index={index} {...metric} />
      ))}
    </div>
  );
}

function VitalTile({ flag, index, label, tone, value, wide = false }) {
  const valueText = String(value ?? "");

  return (
    <div
      className={`tile ${tone} ${wide ? "wide" : ""}`}
      style={{ transitionDelay: `${index * 45}ms` }}
    >
      <div className="tile-label">{label}</div>
      <div className={`tile-value ${valueText.length > 28 ? "small" : ""}`}>
        {valueText}
      </div>
      {flag ? <div className="tile-flag">{flag}</div> : null}
    </div>
  );
}

function EmptyState({ loading }) {
  if (loading) {
    return null;
  }

  return <div className="empty-state">NO ACTIVE SCAN</div>;
}

function getMetrics(data) {
  const status = Number(data.status || 0);
  const responseTime = Number(data.responseTime || 0);
  const wordCount = Number(data.wordCount || 0);
  const h1Count = Number(data.h1Count || 0);
  const missingAltImages = Number(data.missingAltImages || 0);
  const metaDescription = data.metaDescription || "";
  const hasMeta =
    Boolean(metaDescription.trim()) &&
    metaDescription !== "No meta description found";
  const httpOk = status >= 200 && status < 400;
  const responseFast = responseTime < 1000;
  const wordsGood = wordCount >= 300;
  const h1Good = h1Count === 1;
  const altGood = missingAltImages === 0;

  return [
    {
      label: "HTTP Status",
      value: status || "Unknown",
      flag: httpOk ? "OK - Responding" : "WARN - Non-2xx/3xx",
      tone: httpOk ? "ok" : "bad",
    },
    {
      label: "Response Time",
      value: responseTime ? `${responseTime} ms` : "Unknown",
      flag: responseFast ? "OK - Fast" : "WARN - Slow",
      tone: responseFast ? "ok" : "warn",
    },
    {
      label: "Word Count",
      value: wordCount,
      flag: wordsGood ? "OK - Substantial" : "WARN - Thin content",
      tone: wordsGood ? "ok" : "warn",
    },
    {
      label: "H1 Tags",
      value: h1Count,
      flag: h1Good ? "OK - Exactly one" : h1Count === 0 ? "WARN - Missing H1" : "WARN - Multiple H1s",
      tone: h1Good ? "ok" : "warn",
    },
    {
      label: "Missing Alt Text",
      value: missingAltImages,
      flag: altGood ? "OK - All images tagged" : `WARN - ${missingAltImages} untagged`,
      tone: altGood ? "ok" : "warn",
    },
    {
      label: "Meta Description",
      value: hasMeta ? "Present" : "Missing",
      flag: hasMeta ? "OK - Found" : "WARN - Not set",
      tone: hasMeta ? "ok" : "warn",
    },
    {
      label: "Page Title",
      value: data.title || "(empty)",
      tone: "neutral",
      wide: true,
    },
  ];
}

function getHealth(data) {
  if (!data) {
    return {
      label: "STANDBY",
      state: "idle",
      statusText: DEFAULT_STATUS,
    };
  }

  const status = Number(data.status || 0);
  const metaDescription = data.metaDescription || "";
  const httpOk = status >= 200 && status < 400;
  const hasMeta =
    Boolean(metaDescription.trim()) &&
    metaDescription !== "No meta description found";
  const checks = [
    Number(data.wordCount || 0) < 300,
    Number(data.h1Count || 0) !== 1,
    Number(data.missingAltImages || 0) > 0,
    !hasMeta,
  ];
  const warnings = checks.filter(Boolean).length;

  if (!httpOk) {
    return {
      label: "CRITICAL",
      state: "critical",
      statusText: `Scan complete - site returned HTTP ${status || "no response"}.`,
    };
  }

  return {
    label: "HEALTHY",
    state: "healthy",
    statusText:
      warnings > 0
        ? `Scan complete - 200 OK (${warnings} minor content issue${warnings > 1 ? "s" : ""} noted below).`
        : "Scan complete - page looks healthy.",
  };
}


function getErrorTitle(status) {
  if (status === 400) {
    return "INVALID URL";
  }

  if (status === 502) {
    return "FETCH FAILED";
  }

  return status ? `ERROR ${status}` : "CONNECTION FAILED";
}

export default App;
