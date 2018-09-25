package ch.cern.cms.daq.downtimeallocator.model;

public enum AllocationType {

    Downtime, DelayFromSanityCheck, InterventionTime, ReactionTime, ExternalHelp, RecoveryTime, WrongDecision, ImproperToolsUsageOverhead, FirstReaction, SubsequentReactions, CurcialJobTime, OverheadFromBackgroundJobs, Evident, Estimate;
}
