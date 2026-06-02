<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Contract extends Model {
    protected $fillable = [
        'job_listing_id', 'client_id', 'freelance_id', 'proposal_id',
        'amount', 'status', 'payment_status', 'deadline'
    ];

    public function jobListing() { return $this->belongsTo(JobListing::class); }
    public function client() { return $this->belongsTo(User::class, 'client_id'); }
    public function freelance() { return $this->belongsTo(User::class, 'freelance_id'); }
    public function proposal() { return $this->belongsTo(Proposal::class); }
    public function messages() { return $this->hasMany(Message::class); }
    public function reviews() { return $this->hasMany(Review::class); }
}