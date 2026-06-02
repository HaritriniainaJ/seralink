<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Proposal extends Model {
    protected $fillable = [
        'job_listing_id', 'freelance_id',
        'cover_letter', 'amount', 'delivery_days', 'status'
    ];

    public function jobListing() { return $this->belongsTo(JobListing::class); }
    public function freelance() { return $this->belongsTo(User::class, 'freelance_id'); }
    public function contract() { return $this->hasOne(Contract::class); }
}